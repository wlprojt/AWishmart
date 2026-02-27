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

data class GoogleRequest(val idToken: String)

data class TokenResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val email: String
)

data class OtpResponse(
    val ok: Boolean,
    val token: String,
    val user: UserDto? = null
)


data class OkResponse(
    val ok: Boolean = true
)