package com.example.wishmart.auth

import android.content.SharedPreferences
import retrofit2.HttpException
import androidx.core.content.edit

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
) : AuthRepository {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): AuthResult<Unit> {
        return try {
            api.signUp(SignUpRequest(name, email, password))
            AuthResult.OtpSent
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }

    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): AuthResult<Unit> {
        return try {
            api.verifyOtp(VerifyOtpRequest(email, otp))
            AuthResult.OtpVerified
        } catch (e: HttpException) {
            AuthResult.Unauthorized
        } catch (e: Exception) {
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


    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<Unit> {
        return try {
            val response = api.signIn(AuthRequest(email, password))

            // ✅ SAVE TOKEN CORRECTLY
            prefs.edit {
                putString("jwt_token", response.token)
                putString("email", response.user.email)
            }

            AuthResult.Success(Unit)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> AuthResult.Unauthorized
                403 -> AuthResult.Forbidden
                else -> AuthResult.UnknownError
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResult.UnknownError
        }
    }

    override fun getUserEmail(): String? {
        return prefs.getString("email", null)
    }


    override suspend fun sendResetLink(email: String): AuthResult<Unit> {
        return try {
            api.forgotPassword(ForgotPasswordRequest(email))
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }


    override fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }



    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt_token", null)
                ?: return AuthResult.Unauthorized

            api.me("Bearer $token")
            AuthResult.Success(Unit)

        } catch (e: HttpException) {
            AuthResult.Unauthorized
        } catch (e: Exception) {
            AuthResult.UnknownError
        }
    }

    override suspend fun logout() {
        prefs.edit { remove("jwt_token") }
    }

    override fun saveToken(token: String) {
        prefs.edit {
            putString("jwt_token", token)
        }
    }
}