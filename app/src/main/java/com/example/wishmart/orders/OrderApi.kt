package com.example.wishmart.orders

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class BillingDto(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val address: String,
    val apartment: String? = null,
    val country: String,
    val state: String,
    val city: String,
    val postalCode: String
)

data class ItemReq(
    val productId: String,
    val qty: Int
)

data class CreateOrderReq(
    val email: String,
    val billing: BillingDto,
    val items: List<ItemReq>,
    val currency: String = "USD",
    val totalAmount: Double // ✅ ADD THIS
)

data class OrderRes(
    val _id: String,
    val totalAmount: Double,
    val currency: String? = null,
    val paymentStatus: String? = null
)

data class VerifyReq(
    val orderId: String,
    val razorpayOrderId: String,
    val razorpayPaymentId: String,
    val razorpaySignature: String
)

data class SimpleRes(val success: Boolean, val error: String? = null)

interface OrderApi {
    @POST("api/orders/create")
    suspend fun createOrder(@Body body: CreateOrderReq): OrderRes

    @GET("api/orders/my")
    suspend fun myOrders(): List<OrderRes>

    @POST("api/orders/verify")
    suspend fun verifyPayment(@Body body: VerifyReq): SimpleRes
}