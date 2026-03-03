package com.example.wishmart.auth

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val prefs: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = prefs.getString("token", null)

        val path = request.url.encodedPath
        val isAuthEndpoint =
            path.contains("/api/auth/login") ||
                    path.contains("/api/auth/register") ||
                    path.contains("/api/auth/google") ||
                    path.contains("/api/auth/verify-otp") ||
                    path.contains("/api/auth/resend-otp") ||
                    path.contains("/api/auth/forgot-password")

        if (token.isNullOrBlank() || isAuthEndpoint) return chain.proceed(request)

        val newReq = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newReq)
    }
}


