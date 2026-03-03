package com.example.wishmart.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.PaymentActivity
import com.example.wishmart.orders.CreateOrderReq
import com.example.wishmart.orders.OrderApi
import com.example.wishmart.orders.PaymentApi
import com.example.wishmart.orders.PaymentCreateReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderApi: OrderApi,
    private val paymentApi: PaymentApi
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun placeOrderAndPayWithLauncher(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>,
        email: String,
        phone: String,
        createOrderReq: CreateOrderReq,
        totalAmount: Int
    ) {
        viewModelScope.launch {
            if (_loading.value) return@launch

            try {
                _loading.value = true
                _error.value = null

                // 1) Create DB order (pending)
                val dbOrder = orderApi.createOrder(createOrderReq)

                // 2) Create Razorpay order (backend should convert USD->cents)
                val rp = paymentApi.createRazorpayOrder(
                    PaymentCreateReq(amount = totalAmount)
                )

                android.util.Log.d(
                    "RZP",
                    "PAYMENT_RES orderId='${rp.id}' amount=${rp.amount} currency=${rp.currency}"
                )

                val i = Intent(activity, PaymentActivity::class.java).apply {
                    putExtra("rpOrderId", rp.id)
                    putExtra("amount", rp.amount)       // ✅ keep EXACT (smallest unit)
                    putExtra("currency", rp.currency)   // "USD"
                    putExtra("email", email)
                    putExtra("phone", phone)
                    putExtra("dbOrderId", dbOrder._id)
                }

                launcher.launch(i)

            } catch (e: Exception) {
                e.printStackTrace()
                val msg = when (e) {
                    is HttpException -> e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}"
                    else -> e.message
                }
                _error.value = msg ?: "Checkout failed"
            } finally {
                _loading.value = false
            }
        }
    }
}