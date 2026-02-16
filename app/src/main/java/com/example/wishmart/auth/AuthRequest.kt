package com.example.wishmart.auth

// Login
data class AuthRequest(
    val email: String,
    val password: String
)

// Signup
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

// OTP verification
data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class ResendOtpRequest(
    val email: String
)


// Forgot password
data class ForgotPasswordRequest(
    val email: String
)

data class AuthResponse(
    val token: String
)

