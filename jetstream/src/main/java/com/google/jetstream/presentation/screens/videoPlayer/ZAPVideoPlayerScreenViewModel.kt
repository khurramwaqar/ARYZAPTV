package com.google.jetstream.presentation.screens.videoPlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.models.ModelEpisode
import com.google.jetstream.data.models.SeriesN
import com.google.jetstream.data.models.SeriesSingle
import com.google.jetstream.data.network.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ZAPVideoPlayerScreenViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableLiveData<ZAPVideoPlayerScreenUiState>()
    val uiState: LiveData<ZAPVideoPlayerScreenUiState> = _uiState
    private val seriesId: String? = savedStateHandle[ZAPVideoPlayerScreen.ZPlayerBundleId]

    init {
        fetchSeriesById(seriesId)
    }

    private fun fetchSeriesById(seriesId: String?) {
        viewModelScope.launch {
            _uiState.value = ZAPVideoPlayerScreenUiState.Loading
            try {
                val seriesResponse = RetrofitClient.apiService.getSingleSeriesN(seriesId!!)

                _uiState.value = ZAPVideoPlayerScreenUiState.Ready(seriesResponse)
                Log.d("returnDataSDS", _uiState.value.toString())
            } catch (e: Exception) {
                _uiState.value = ZAPVideoPlayerScreenUiState.Error
                Log.d("returnDataeSDR", e.message.toString())
            }
        }
    }


}



sealed class ZAPVideoPlayerScreenUiState {
    object Loading : ZAPVideoPlayerScreenUiState()
    object Error : ZAPVideoPlayerScreenUiState()
    data class Ready(val series: SeriesSingle) : ZAPVideoPlayerScreenUiState()
}