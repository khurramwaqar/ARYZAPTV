/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.entities.MovieCategoryList
import com.google.jetstream.data.models.ModelCategories
import com.google.jetstream.data.network.RetrofitClient
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ZCategoriesScreenViewModel: ViewModel() {
    private val _uiState = MutableLiveData<ZCategoriesScreenUiState>()
    val uiState: LiveData<ZCategoriesScreenUiState> = _uiState

    init {
        fetchCategories()
    }

//    val uiState = movieRepository.getMovieCategories().map {
//        ZCategoriesScreenUiState.Ready(categoryList = it)
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = ZCategoriesScreenUiState.Loading
//    )

    private fun fetchCategories() {
        viewModelScope.launch {
            _uiState.value = ZCategoriesScreenUiState.Loading
            try {
                val response = RetrofitClient.apiService.getCategories()
                _uiState.value = ZCategoriesScreenUiState.Ready(response.toList())
                Log.d("returnDatas", _uiState.value.toString())
            } catch (e: Exception) {
                _uiState.value = ZCategoriesScreenUiState.Error
                Log.d("returnDatae", e.localizedMessage.toString())
            }
        }
    }

}

sealed class ZCategoriesScreenUiState {

    object Loading: ZCategoriesScreenUiState()
    data class Ready(val categoryList: List<ModelCategories>): ZCategoriesScreenUiState()

    object Error : ZCategoriesScreenUiState()
}