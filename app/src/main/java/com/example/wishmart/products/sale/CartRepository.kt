package com.example.wishmart.products.sale

import javax.inject.Inject

class CartRepository @Inject constructor(
    private val api: CartApi
) {

    suspend fun addToCart(productId: String, qty: Int) {
        api.addToCart(
            AddToCartRequest(productId, qty)
        )
    }

    suspend fun getCart(): List<CartItem> {
        return api.getCart().items
    }

    suspend fun updateQty(id: String, qty: Int) {
        api.updateQty(UpdateQtyRequest(id, qty))
    }

    suspend fun removeItem(id: String) {
        api.removeItem(RemoveItemRequest(id))
    }
}

