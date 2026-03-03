package com.example.wishmart.search


import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

data class SuggestionDto(
    val _id: String,
    val title: String,
    val images: List<String>
)

interface SearchApi {
    @GET("api/products/suggestions")
    suspend fun suggestions(
        @Query("q") query: String
    ): List<SuggestionDto>
}

class SearchRepository @Inject constructor(
    private val api: SearchApi
) {
    suspend fun getSuggestions(q: String): List<SuggestionDto> =
        api.suggestions(q)
}