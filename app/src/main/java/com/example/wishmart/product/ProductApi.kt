package com.example.wishmart.product

// ProductApi.kt

import retrofit2.http.GET
import retrofit2.http.Query

data class ProductsResponse(
    val products: List<ProductDto>,
    val total: Int,
    val page: Int,
    val pages: Int
)

data class ProductDto(
    val _id: String,
    val title: String,
    val price: Double,
    val sale_price: Double?,
    val images: List<String>,
    val category: String,
    val rating: Double,
    val rating_count: Int,
    val stock: Int
) {
    val displayPrice: Double get() = sale_price ?: price
    val hasSale: Boolean get() = sale_price != null
}

interface ProductApi {
    @GET("api/products/all")
    suspend fun allProducts(
        @Query("page") page: Int,
        @Query("category") category: String? = null,
        @Query("sort") sort: String? = null,
        @Query("minPrice") minPrice: Int? = null,
        @Query("maxPrice") maxPrice: Int? = null
    ): ProductsResponse
}

interface SaleApi {
    @GET("api/products/sale")
    suspend fun saleProducts(): List<ProductDto>
}