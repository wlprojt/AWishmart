package com.example.wishmart.ui

data class AuthState(
    val signUpName: String = "",
    val signUpUsername: String = "",
    val signUpPassword: String = "",
    val signInUsername: String = "",
    val signInPassword: String = "",
    val isLoading: Boolean = false
)