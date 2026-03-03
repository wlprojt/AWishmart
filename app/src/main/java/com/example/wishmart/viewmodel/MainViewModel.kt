package com.example.wishmart.viewmodel

import com.example.wishmart.auth.AuthRepository
import com.example.wishmart.auth.AuthResult
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.auth.AuthApi
import com.example.wishmart.auth.GoogleRequest
import com.example.wishmart.ui.AuthState
import com.example.wishmart.ui.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val authApi: AuthApi // ✅ use AuthApi for google too
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    var state by mutableStateOf(AuthState())
        private set

    private val resultChannel = Channel<AuthResult<*>>(Channel.BUFFERED)
    val authResults = resultChannel.receiveAsFlow()

    init {
        _isLoggedIn.value = !repository.getToken().isNullOrBlank()
        _email.value = repository.getUserEmail()
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.SignIn -> signIn()
            is AuthUiEvent.SignUp -> signUp()
            is AuthUiEvent.GoogleSignIn -> googleSignIn(event.idToken)

            is AuthUiEvent.SignInUsernameChanged ->
                state = state.copy(signInUsername = event.value)

            is AuthUiEvent.SignInPasswordChanged ->
                state = state.copy(signInPassword = event.value)

            is AuthUiEvent.SignUpNameChanged ->
                state = state.copy(signUpName = event.value)

            is AuthUiEvent.SignUpUsernameChanged ->
                state = state.copy(signUpUsername = event.value)

            is AuthUiEvent.SignUpPasswordChanged ->
                state = state.copy(signUpPassword = event.value)

            is AuthUiEvent.VerifyOtp -> verifyOtp(event.email, event.otp)
            is AuthUiEvent.ResendOtp -> resendOtp(event.email)
            else -> {}
        }
    }

    private fun signUp() = viewModelScope.launch {
        state = state.copy(isLoading = true)

        val result = repository.signUp(
            name = state.signUpName,
            email = state.signUpUsername,
            password = state.signUpPassword
        )

        if (result is AuthResult.OtpSent) {
            _email.value = state.signUpUsername  // ✅ OTP screen can use this
        }

        resultChannel.send(result)
        state = state.copy(isLoading = false)
    }

    private fun verifyOtp(email: String, otp: String) = viewModelScope.launch {
        state = state.copy(isLoading = true)

        val finalEmail = email.trim().ifBlank { repository.getUserEmail().orEmpty() }
        val cleanOtp = otp.trim()

        val result = repository.verifyOtp(finalEmail, cleanOtp)

        if (result == AuthResult.OtpVerified) {
            _isLoggedIn.value = true
            _email.value = repository.getUserEmail()

            val t = repository.getToken()
            android.util.Log.d("OTP", "after verify token=${t?.take(15)}")
        }

        resultChannel.send(result)
        state = state.copy(isLoading = false)
    }

    private fun resendOtp(email: String) = viewModelScope.launch {
        val result = repository.resendOtp(email)
        resultChannel.send(result)
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

    /** ✅ GOOGLE LOGIN (call /api/auth/google, not /google-login) */
    private fun googleSignIn(idToken: String) = viewModelScope.launch {
        state = state.copy(isLoading = true)

        try {
            val res = authApi.google(GoogleRequest(idToken))

            repository.saveToken(res.token)
            repository.saveUserEmail(res.user.email)

            _isLoggedIn.value = true
            _email.value = res.user.email

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