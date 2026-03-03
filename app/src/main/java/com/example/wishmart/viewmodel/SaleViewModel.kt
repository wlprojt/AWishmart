package com.example.wishmart.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.product.ProductDto
import com.example.wishmart.product.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SaleUiState(
    val items: List<ProductDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val repo: SaleRepository
) : ViewModel() {

    var state by mutableStateOf(SaleUiState())
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val res = repo.getSaleProducts()
                state = state.copy(items = res, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load sale products"
                )
            }
        }
    }
}