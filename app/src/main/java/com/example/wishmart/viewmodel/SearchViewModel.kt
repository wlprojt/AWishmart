package com.example.wishmart.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishmart.search.SearchRepository
import com.example.wishmart.search.SuggestionDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepository
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    var suggestions by mutableStateOf<List<SuggestionDto>>(emptyList())
        private set

    private var job: Job? = null

    fun onQueryChange(text: String) {
        query = text
        job?.cancel()

        if (text.length < 2) {
            suggestions = emptyList()
            return
        }

        job = viewModelScope.launch {
            delay(300) // debounce
            try {
                suggestions = repo.getSuggestions(text)
            } catch (_: Exception) {
                suggestions = emptyList()
            }
        }
    }

    fun clear() {
        query = ""
        suggestions = emptyList()
    }
}