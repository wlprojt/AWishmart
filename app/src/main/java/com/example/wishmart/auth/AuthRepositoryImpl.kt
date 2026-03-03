package com.example.wishmart.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
) : AuthRepository {

    private val KEY_TOKEN = "token"
    private val KEY_EMAIL = "email"

    override suspend fun signUp(name: String, email: String, password: String): AuthResult<Unit> {
        return try {
            api.register(SignUpRequest(name, email, password))
            prefs.edit { putString(KEY_EMAIL, email) }
            AuthResult.OtpSent
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): AuthResult<Unit> {
        return try {
            val e = email.trim()
            val o = otp.trim()

            val res = api.verifyOtp(VerifyOtpRequest(e, o)) // ✅ OtpResponse

            // ✅ backend must send ok=true + token
            if (!res.ok || res.token.isBlank()) {
                return AuthResult.UnknownError
            }

            // ✅ user can be missing; don't crash
            val finalEmail = res.user?.email?.trim().takeUnless { it.isNullOrBlank() } ?: e

            prefs.edit {
                putString(KEY_TOKEN, res.token)
                putString(KEY_EMAIL, finalEmail)
            }

            AuthResult.OtpVerified
        } catch (e: HttpException) {
            when (e.code()) {
                400, 401 -> AuthResult.Unauthorized // invalid/expired OTP
                else -> AuthResult.UnknownError
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResult.UnknownError
        }
    }

    override suspend fun resendOtp(email: String): AuthResult<Unit> {
        return try {
            api.resendOtp(ResendOtpRequest(email))
            AuthResult.OtpSent
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            val res = api.login(AuthRequest(email, password))

            prefs.edit {
                putString(KEY_TOKEN, res.token)
                putString(KEY_EMAIL, res.user.email)
            }

            AuthResult.Success(Unit)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> AuthResult.Unauthorized
                403 -> AuthResult.Forbidden
                else -> AuthResult.UnknownError
            }
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }

    override fun getUserEmail(): String? = prefs.getString(KEY_EMAIL, null)

    override fun saveUserEmail(email: String) {
        prefs.edit { putString(KEY_EMAIL, email.trim()) }
    }

    override suspend fun sendResetLink(email: String): AuthResult<Unit> {
        return try {
            api.forgotPassword(ForgotPasswordRequest(email))
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            if (getToken().isNullOrBlank()) return AuthResult.Unauthorized
            api.me() // ✅ interceptor adds Authorization
            AuthResult.Success(Unit)
        } catch (e: HttpException) {
            AuthResult.Unauthorized
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }

    override suspend fun logout() {
        prefs.edit {
            remove(KEY_TOKEN)
            remove(KEY_EMAIL)
        }
    }

    override fun saveToken(token: String) {
        prefs.edit { putString(KEY_TOKEN, token) }
    }

    override fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
}