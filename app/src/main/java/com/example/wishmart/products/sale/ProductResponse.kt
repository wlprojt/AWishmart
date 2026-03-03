package com.example.wishmart.products.sale

import com.example.wishmart.auth.OkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class ProductsResponse(
    val products: List<ProductResponse>
)

data class CartItem(
    val _id: String,
    val productId: String,
    val title: String,
    val price: Double,
    val sale_price: Double?,
    val image: String,
    val qty: Int,
    val stock: Int
)

data class CartUiState(
    val isLoading: Boolean = false,
    val items: List<CartItem> = emptyList(),
    val error: String? = null
)

data class AddToCartRequest(
    val productId: String,
    val qty: Int
)


data class CartResponse(
    val items: List<CartItem>
)

data class UpdateQtyRequest(
    val id: String,
    val qty: Int
)

data class RemoveItemRequest(
    val id: String
)

data class ApiResponse(
    val ok: Boolean? = null,
    val error: String? = null
)


data class ProductResponse(
    val _id: String,
    val title: String,
    val price: Double,
    val sale_price: Double?,
    val images: List<String>?,
    val rating: Double,
    val rating_count: Int,
    val category: String,
    val description: String,
    val stock: Int
)

data class GoogleLoginRequest(
    val idToken: String
)

data class GoogleLoginResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String
)


interface BackendApi {

    // Send Google ID token to backend → returns JWT
    @POST("/api/auth/google-login")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): GoogleLoginResponse
}

interface CartApi {

    @POST("api/cart")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): ApiResponse

    @GET("api/cart")
    suspend fun getCart(): CartResponse

    @PUT("api/cart")
    suspend fun updateQty(
        @Body request: UpdateQtyRequest
    ): ApiResponse

    @HTTP(method = "DELETE", path = "api/cart", hasBody = true)
    suspend fun removeItem(
        @Body request: RemoveItemRequest
    ): ApiResponse

    @POST("api/cart/clear")
    suspend fun clearCart(): OkResponse
}


interface ProductsApi {

    @GET("api/products/sale")
    suspend fun getSaleProducts(): List<ProductResponse>

    @GET("api/products")
    suspend fun getProductsByCategory(
        @Query("category") category: String
    ): List<ProductResponse>

    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): ProductResponse


}
