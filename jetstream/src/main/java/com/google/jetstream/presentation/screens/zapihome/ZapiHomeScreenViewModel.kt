package com.google.jetstream.presentation.screens.zapihome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.models.HomeDatum
import com.google.jetstream.data.network.ApiService
import com.google.jetstream.data.network.createRetrofit
import kotlinx.coroutines.launch

class ZapiHomeScreenViewModel: ViewModel() {
    private val retrofit = createRetrofit().create(ApiService::class.java)

    var homeList by mutableStateOf<List<HomeDatum>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        fetchHome()
    }

    public fun fetchHome() {
        viewModelScope.launch {
            isLoading = true
            try {

            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
}