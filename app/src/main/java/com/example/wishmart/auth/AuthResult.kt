package com.example.wishmart.auth

sealed class AuthResult<out T> {
    object OtpSent : AuthResult<Nothing>()
    object OtpVerified : AuthResult<Nothing>()
    object Unauthorized : AuthResult<Nothing>()
    object Forbidden : AuthResult<Nothing>()
    data class Success<T>(val data: T) : AuthResult<T>()
    object UnknownError : AuthResult<Nothing>()
}
