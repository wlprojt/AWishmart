package com.example.wishmart.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.products.sale.ProductRepository
import com.example.wishmart.products.sale.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    var audioVideoProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set
    var homeAppliancesProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set
    var airConditionerProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set
    var kitchenAppliancesProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set
    var refrigeratorProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set
    var pcLaptopsProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set

    var gadgetsProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set

    var selectedProduct by mutableStateOf<ProductResponse?>(null)
        private set

    var relatedProducts by mutableStateOf<List<ProductResponse>>(emptyList())
        private set

    init {
        fetchAudioVideo()
        fetchHomeAppliances()
        fetchAirConditioner()
        fetchKitchenAppliances()
        fetchRefrigerators()
        fetchPCsLaptops()
        fetchGadgets()
    }

    private fun fetchAudioVideo() {
        viewModelScope.launch {
            try {
                audioVideoProducts = repository.getAudioVideoProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun fetchHomeAppliances() {
        viewModelScope.launch {
            try {
                homeAppliancesProducts = repository.getHomeAppliancesProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun fetchAirConditioner() {
        viewModelScope.launch {
            try {
                airConditionerProducts = repository.getAirConditionerProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun fetchKitchenAppliances() {
        viewModelScope.launch {
            try {
                kitchenAppliancesProducts = repository.getKitchenAppliancesProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchRefrigerators() {
        viewModelScope.launch {
            try {
                refrigeratorProducts = repository.getRefrigeratorsProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchPCsLaptops() {
        viewModelScope.launch {
            try {
                pcLaptopsProducts = repository.getPCsLaptopsProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchGadgets() {
        viewModelScope.launch {
            try {
                gadgetsProducts = repository.getGadgetsProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchProductById(id: String) {
        viewModelScope.launch {
            try {
                selectedProduct = repository.getProductById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRelatedProducts(category: String, currentId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getProductsByCategory(category)

                relatedProducts = response
                    .filter { it._id != currentId }  // 🚀 remove same product
                    .distinctBy { it._id }
                    .take(6)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
