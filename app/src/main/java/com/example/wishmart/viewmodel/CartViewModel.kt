package com.example.wishmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.products.sale.CartItem
import com.example.wishmart.products.sale.CartRepository
import com.example.wishmart.products.sale.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val items: List<CartItem>) : CartUiState()
    data class Error(val message: String) : CartUiState()
}


@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState

    init {
        fetchCart()
    }

    fun addToCart(product: ProductResponse) {

        viewModelScope.launch {
            try {
                repository.addToCart(
                    productId = product._id,
                    qty = 1
                )

                // Optional: refresh cart
                fetchCart()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /* ---------------- FETCH CART ---------------- */

    fun fetchCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading

            try {
                val items = repository.getCart()

                _uiState.value = CartUiState.Success(items)

            } catch (e: Exception) {
                _uiState.value =
                    CartUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    /* ---------------- UPDATE QTY ---------------- */

    fun updateQty(id: String, newQty: Int) {
        val currentState = _uiState.value
        if (currentState !is CartUiState.Success) return

        val updatedList = currentState.items.map {
            if (it._id == id) {
                it.copy(qty = newQty.coerceAtLeast(1))
            } else it
        }

        _uiState.value = CartUiState.Success(updatedList)

        viewModelScope.launch {
            try {
                repository.updateQty(id, newQty)
            } catch (e: Exception) {
                fetchCart() // rollback if failed
            }
        }
    }

    /* ---------------- REMOVE ITEM ---------------- */

    fun removeItem(id: String) {
        val currentState = _uiState.value
        if (currentState !is CartUiState.Success) return

        val updatedList = currentState.items.filter { it._id != id }
        _uiState.value = CartUiState.Success(updatedList)

        viewModelScope.launch {
            try {
                repository.removeItem(id)
            } catch (e: Exception) {
                fetchCart() // rollback if failed
            }
        }
    }

    /* ---------------- TOTAL ---------------- */

    fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf {
            (it.sale_price ?: it.price) * it.qty
        }
    }

    fun clearCart() = viewModelScope.launch {
        try {
            repository.clearCart() // call api/cart/clear
            _uiState.value = CartUiState.Success(emptyList())
        } catch (e: Exception) {
            // fallback: still clear local
            _uiState.value = CartUiState.Success(emptyList())
        }
    }
}
