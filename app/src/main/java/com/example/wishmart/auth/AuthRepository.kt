package com.example.wishmart.auth

interface AuthRepository {

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): AuthResult<Unit>

    suspend fun verifyOtp(
        email: String,
        otp: String
    ): AuthResult<Unit>

    suspend fun resendOtp(email: String): AuthResult<Unit>

    suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<Unit>

    fun getUserEmail(): String?

    fun saveUserEmail(email: String)

    suspend fun sendResetLink(email: String): AuthResult<Unit>

    suspend fun authenticate(): AuthResult<Unit>

    suspend fun logout()

    // 🔑 token helpers
    fun saveToken(token: String)
    fun getToken(): String?
}