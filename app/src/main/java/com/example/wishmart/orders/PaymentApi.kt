package com.example.wishmart.orders

import retrofit2.http.Body
import retrofit2.http.POST

data class PaymentCreateReq(val amount: Int)

data class PaymentCreateRes(
    val id: String,
    val amount: Int,
    val currency: String,
    val keyId: String
)

interface PaymentApi {
    @POST("api/payment")
    suspend fun createRazorpayOrder(@Body body: PaymentCreateReq): PaymentCreateRes
}