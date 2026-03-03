package com.example.wishmart.products.sale

import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductsApi
) {
    suspend fun getSaleProducts(): List<ProductResponse> {
        return api.getSaleProducts()
    }

    suspend fun getAudioVideoProducts(): List<ProductResponse> {
        return api.getProductsByCategory("Audio & Video")
    }

    suspend fun getHomeAppliancesProducts(): List<ProductResponse> {
        return api.getProductsByCategory("Home Appliances")
    }

    suspend fun getAirConditionerProducts(): List<ProductResponse> {
        return api.getProductsByCategory("Air Conditioner")
    }

    suspend fun getKitchenAppliancesProducts(): List<ProductResponse> {
        return api.getProductsByCategory("Kitchen Appliances")
    }
    suspend fun getRefrigeratorsProducts(): List<ProductResponse> {
        return api.getProductsByCategory("Refrigerator")
    }
    suspend fun getPCsLaptopsProducts(): List<ProductResponse> {
        return api.getProductsByCategory("PCs & Laptop")
    }
    suspend fun getGadgetsProducts(): List<ProductResponse> {
        return api.getProductsByCategory("Gadgets")
    }

    suspend fun getProductById(id: String): ProductResponse {
        return api.getProductById(id)
    }

    suspend fun getProductsByCategory(category: String): List<ProductResponse> {
        return api.getProductsByCategory(category)
    }

}

