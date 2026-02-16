package com.example.wishmart.viewmodel

import com.example.wishmart.auth.AuthRepository
import com.example.wishmart.auth.AuthResult
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.products.sale.BackendApi
import com.example.wishmart.products.sale.GoogleLoginRequest
import com.example.wishmart.ui.AuthState
import com.example.wishmart.ui.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import android.util.Base64


@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val repository: AuthRepository,
    private val backendApi: BackendApi // Add your backend API
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    var state by mutableStateOf(AuthState())
        private set

    private val resultChannel = Channel<AuthResult<Unit>>(Channel.BUFFERED)
    val authResults = resultChannel.receiveAsFlow()

    init {
        val token = prefs.getString("jwt_token", null)

        _isLoggedIn.value = !token.isNullOrEmpty()

        _email.value = repository.getUserEmail()

    }


    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.SignIn -> signIn()
            is AuthUiEvent.GoogleSignIn -> googleSignIn(event.idToken)
            is AuthUiEvent.SignInUsernameChanged -> state = state.copy(signInUsername = event.value)
            is AuthUiEvent.SignInPasswordChanged -> state = state.copy(signInPassword = event.value)
            else -> {}
        }
    }


    private fun signIn() = viewModelScope.launch {
        state = state.copy(isLoading = true)

        val result = repository.signIn(
            email = state.signInUsername,
            password = state.signInPassword
        )

        if (result is AuthResult.Success) {
            _isLoggedIn.value = true
            _email.value = repository.getUserEmail()
        }

        resultChannel.send(result)
        state = state.copy(isLoading = false)
    }

    private fun extractEmailFromJwt(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            json.optString("email")
        } catch (e: Exception) {
            null
        }
    }

    /** ---------------- GOOGLE LOGIN ---------------- */
    private fun googleSignIn(idToken: String) = viewModelScope.launch {
        state = state.copy(isLoading = true)

        try {
            val response = backendApi.googleLogin(GoogleLoginRequest(idToken))
            val jwtToken = response.token

            prefs.edit().putString("jwt_token", jwtToken).apply()

            _isLoggedIn.value = true

            // ✅ Use backend response directly
            _email.value = response.user.email

            resultChannel.send(AuthResult.Success(Unit))

        } catch (e: Exception) {
            resultChannel.send(AuthResult.UnknownError)
        } finally {
            state = state.copy(isLoading = false)
        }
    }



    fun logout() = viewModelScope.launch {
        repository.logout()
        _isLoggedIn.value = false
        _email.value = null
    }
}
