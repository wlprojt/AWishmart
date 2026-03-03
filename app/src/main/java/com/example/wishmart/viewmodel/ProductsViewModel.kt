package com.example.wishmart.viewmodel

// ProductsViewModel.kt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.product.ProductDto
import com.example.wishmart.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductsUiState(
    val items: List<ProductDto> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,

    val page: Int = 1,
    val pages: Int = 1,
    val total: Int = 0,

    val category: String? = null,
    val sort: String? = "latest",
    val minPrice: Int? = null,
    val maxPrice: Int? = null
)

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {

    var state by mutableStateOf(ProductsUiState())
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, page = 1)

            try {
                val res = repo.getProducts(
                    page = 1,
                    category = state.category,
                    sort = state.sort,
                    minPrice = state.minPrice,
                    maxPrice = state.maxPrice
                )
                state = state.copy(
                    isLoading = false,
                    items = res.products,
                    page = res.page,
                    pages = res.pages,
                    total = res.total
                )
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Something went wrong")
            }
        }
    }

    fun loadMore() {
        if (state.isLoading || state.isLoadingMore) return
        if (state.page >= state.pages) return

        val nextPage = state.page + 1

        viewModelScope.launch {
            state = state.copy(isLoadingMore = true, error = null)

            try {
                val res = repo.getProducts(
                    page = nextPage,
                    category = state.category,
                    sort = state.sort,
                    minPrice = state.minPrice,
                    maxPrice = state.maxPrice
                )
                state = state.copy(
                    isLoadingMore = false,
                    items = state.items + res.products,
                    page = res.page,
                    pages = res.pages,
                    total = res.total
                )
            } catch (e: Exception) {
                state = state.copy(isLoadingMore = false, error = e.message ?: "Failed to load more")
            }
        }
    }

    fun setCategory(category: String?) {
        state = state.copy(category = category)
        refresh()
    }

    fun setSort(sort: String?) {
        state = state.copy(sort = sort)
        refresh()
    }

    fun setPrice(min: Int?, max: Int?) {
        state = state.copy(minPrice = min, maxPrice = max)
        refresh()
    }

    fun clearFilters() {
        state = state.copy( sort = "latest", minPrice = null, maxPrice = null)
        refresh()
    }
}