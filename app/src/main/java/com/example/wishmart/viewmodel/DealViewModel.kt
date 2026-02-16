package com.example.wishmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.products.sale.ProductRepository
import com.example.wishmart.products.sale.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DealViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products =
        MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products

    init {
        fetchDeals()
    }

    private fun fetchDeals() {
        viewModelScope.launch {
            try {
                _products.value = repository.getSaleProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

