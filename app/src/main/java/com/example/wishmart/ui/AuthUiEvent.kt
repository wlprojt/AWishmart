package com.example.wishmart.ui

sealed class AuthUiEvent {
    data class SignUpNameChanged(val value: String) : AuthUiEvent()
    data class SignUpUsernameChanged(val value: String) : AuthUiEvent()
    data class SignUpPasswordChanged(val value: String) : AuthUiEvent()
    object SignUp : AuthUiEvent()

    data class VerifyOtp(val email: String, val otp: String) : AuthUiEvent()
    data class ResendOtp(val email: String) : AuthUiEvent()

    data class SignInUsernameChanged(val value: String) : AuthUiEvent()
    data class SignInPasswordChanged(val value: String) : AuthUiEvent()
    object SignIn : AuthUiEvent()

    data class GoogleSignIn(val idToken: String) : AuthUiEvent()

    object ForgotPassword : AuthUiEvent()
}