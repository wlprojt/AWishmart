package com.example.wishmart.product

// ProductRepository.kt

import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductApi
) {
    suspend fun getProducts(
        page: Int,
        category: String?,
        sort: String?,
        minPrice: Int?,
        maxPrice: Int?
    ): ProductsResponse {
        return api.allProducts(
            page = page,
            category = category,
            sort = sort,
            minPrice = minPrice,
            maxPrice = maxPrice
        )
    }
}

class SaleRepository @Inject constructor(
    private val api: SaleApi
) {
    suspend fun getSaleProducts(): List<ProductDto> = api.saleProducts()
}