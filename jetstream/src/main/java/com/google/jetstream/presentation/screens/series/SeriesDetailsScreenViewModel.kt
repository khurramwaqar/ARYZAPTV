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

package com.google.jetstream.presentation.screens.series

import Favorites
import SeriesList
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.models.ModelEpisode
import com.google.jetstream.data.models.Series
import com.google.jetstream.data.models.SeriesSingle
import com.google.jetstream.data.network.RetrofitClient
import com.google.jetstream.data.repositories.FavoritesRepository
import com.google.jetstream.data.repositories.MovieRepository
import com.google.jetstream.presentation.screens.movies.SeriesDetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailsScreenViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableLiveData<SeriesDetailsScreenUiState>()
    val uiState: LiveData<SeriesDetailsScreenUiState> = _uiState
    private val seriesId: String? = savedStateHandle["seriedId"]

    init {
        fetchSeriesById(seriesId)
    }

    private fun fetchSeriesById(seriesId: String?) {
        viewModelScope.launch {
            _uiState.value = SeriesDetailsScreenUiState.Loading
            try {
                val seriesResponse = RetrofitClient.apiService.getSingleSeries(seriesId!!)
                val episodeResponse = RetrofitClient.apiService.getEpisodeBySeriesId(seriesId!!)

                val combineResult = CombineSeriesAndEpisode(seriesResponse, episodeResponse)

                _uiState.value = SeriesDetailsScreenUiState.Ready(combineResult)
                Log.d("returnDataSDS", _uiState.value.toString())
            } catch (e: Exception) {
                _uiState.value = SeriesDetailsScreenUiState.Error
                Log.d("returnDataeSDR", e.message.toString())
            }
        }
    }


}

data class CombineSeriesAndEpisode(
    val series: SeriesSingle,
    val details: ModelEpisode
)

sealed class SeriesDetailsScreenUiState {
    object Loading : SeriesDetailsScreenUiState()
    object Error : SeriesDetailsScreenUiState()
    data class Ready(val combineSeriesAndEpisode: CombineSeriesAndEpisode) : SeriesDetailsScreenUiState()
}