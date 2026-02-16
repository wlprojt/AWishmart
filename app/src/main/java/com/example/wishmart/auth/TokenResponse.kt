package com.example.wishmart.auth

data class TokenResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val email: String
)