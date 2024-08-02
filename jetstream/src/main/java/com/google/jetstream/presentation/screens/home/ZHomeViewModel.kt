import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.models.HomeDatum
import com.google.jetstream.data.network.RetrofitClient
import kotlinx.coroutines.launch

class ZHomeViewModel : ViewModel() {
    private val _uiState = MutableLiveData<ZHomeScreenUiState>()
    val uiState: LiveData<ZHomeScreenUiState> = _uiState

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = ZHomeScreenUiState.Loading
            try {
                val response = RetrofitClient.apiService.getHomeData()
                _uiState.value = ZHomeScreenUiState.Ready(response.home.homeData)
                Log.d("returnDatas", _uiState.value.toString())
            } catch (e: Exception) {
                _uiState.value = ZHomeScreenUiState.Error
                Log.d("returnDatae", e.localizedMessage.toString())
            }
        }
    }
}

sealed class ZHomeScreenUiState {
    object Loading : ZHomeScreenUiState()
    data class Ready(val homeData: List<HomeDatum>) : ZHomeScreenUiState()
    object Error : ZHomeScreenUiState()
}