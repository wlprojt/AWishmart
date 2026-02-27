package com.example.wishmart.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/google")
    suspend fun google(@Body request: GoogleRequest): TokenResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: SignUpRequest): OkResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): TokenResponse

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): OtpResponse

    @POST("api/auth/resend-otp")
    suspend fun resendOtp(@Body request: ResendOtpRequest): OkResponse

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): OkResponse

    @GET("api/auth/me")
    suspend fun me(): UserDto
}