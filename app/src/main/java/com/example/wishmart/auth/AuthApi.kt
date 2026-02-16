package com.example.wishmart.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    )

    @POST("api/auth/login")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    )


    @POST("/api/auth/resend-otp")
    suspend fun resendOtp(
        @Body request: ResendOtpRequest
    )


    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    )


    @GET("api/auth/me")
    suspend fun me(
        @Header("Authorization") token: String
    ): UserDto

}
