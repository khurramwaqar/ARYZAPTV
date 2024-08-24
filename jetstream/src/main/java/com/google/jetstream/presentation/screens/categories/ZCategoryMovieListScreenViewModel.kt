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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.models.ModelSeriesByCatTitle
import com.google.jetstream.data.network.RetrofitClient
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ZCategoryMovieListScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData<ZCategoryMovieListScreenUiState>()
    val uiState: LiveData<ZCategoryMovieListScreenUiState> = _uiState
    private val seriesId: String? = savedStateHandle["categoryId"]

    init {
        fetchSeriesByCategoriesTitle(seriesId)
    }
//    val uiState =
//        savedStateHandle.getStateFlow<String?>(
//            CategoryMovieListScreen.CategoryIdBundleKey,
//            null
//        ).map { id ->
//            if (id == null) {
//                ZCategoryMovieListScreenUiState.Error
//            } else {
//                val categoryDetails = movieRepository.getMovieCategoryDetails(id)
//                ZCategoryMovieListScreenUiState.Done(categoryDetails)
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = ZCategoryMovieListScreenUiState.Loading
//        )

    private fun fetchSeriesByCategoriesTitle(seriesId: String?) {
        viewModelScope.launch {
            _uiState.value = ZCategoryMovieListScreenUiState.Loading
            try {
                val response = RetrofitClient.apiService.getSeriesByCatName(seriesId.toString())
                _uiState.value = ZCategoryMovieListScreenUiState.Done(response, seriesId.toString())
                Log.d("returnDatas", _uiState.value.toString())
            } catch (e: Exception) {
                _uiState.value = ZCategoryMovieListScreenUiState.Error
                Log.d("returnDatae", e.localizedMessage.toString())
            }
        }
    }

}

sealed class ZCategoryMovieListScreenUiState {
    object Loading : ZCategoryMovieListScreenUiState()
    object Error : ZCategoryMovieListScreenUiState()
    data class Done(val seriesByCatTitle: ModelSeriesByCatTitle, val title: String) : ZCategoryMovieListScreenUiState()
}