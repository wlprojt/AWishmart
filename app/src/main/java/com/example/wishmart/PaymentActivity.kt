package com.example.wishmart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : ComponentActivity(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout.preload(applicationContext)

        val rpOrderId = intent.getStringExtra("rpOrderId") ?: run { finish(); return }
        val amount = intent.getIntExtra("amount", 0)
        val currency = intent.getStringExtra("currency") ?: "USD"
        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")

        if (amount <= 0) { finish(); return }

        openCheckout(rpOrderId, amount, currency, email, phone)
    }

    private fun openCheckout(
        rpOrderId: String,
        amount: Int,
        currency: String,
        email: String?,
        phone: String?
    ) {
        try {
            val checkout = Checkout()
            checkout.setKeyID("rzp_live_Rij9VeABLoKAD1")

            val options = JSONObject().apply {
                put("name", "Wishmart")
                put("description", "Order Payment")
                put("order_id", rpOrderId)
                put("currency", currency)
                put("amount", amount)

                put("prefill", JSONObject().apply {
                    if (!email.isNullOrBlank()) put("email", email)
                    if (!phone.isNullOrBlank()) put("contact", phone)
                })

                // USD: force card to avoid blank methods
                put("method", JSONObject().apply {
                    put("card", true)
                    put("upi", false)
                    put("wallet", false)
                    put("netbanking", false)
                })
            }

            Log.d("RZP", "OPEN options=$options")
            checkout.open(this@PaymentActivity, options)

        } catch (t: Throwable) {
            Log.e("RZP", "checkout.open failed", t)
            Toast.makeText(this, "Checkout error: ${t.message}", Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onPaymentSuccess(paymentId: String?) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("paymentId", paymentId)
        })
        finish()
    }

    override fun onPaymentError(code: Int, description: String?) {
        setResult(Activity.RESULT_CANCELED, Intent().apply {
            putExtra("errorCode", code)
            putExtra("errorDesc", description)
        })
        finish()
    }
}