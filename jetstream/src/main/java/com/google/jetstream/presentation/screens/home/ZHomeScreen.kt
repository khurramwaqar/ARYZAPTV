package com.google.jetstream.presentation.screens.dashboard

import ZHomeViewModel
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import com.google.jetstream.data.models.Episode
import com.google.jetstream.data.models.HomeDatum
import com.google.jetstream.data.models.Series
import com.google.jetstream.presentation.common.SexySeriesCarousel
import com.google.jetstream.presentation.common.ZNMoviesRow
import com.google.jetstream.presentation.screens.home.SingleSeriesCarousel
import com.google.jetstream.presentation.screens.home.ZFeaturedSlider
import com.google.jetstream.presentation.screens.movies.MoviesScreenMovieList
import com.google.jetstream.presentation.screens.movies.SeriesScreenMovieList

@Composable
fun ZHomeScreen(
    onClick: ()-> Unit,
    onSeriesClick: (movie: Series) -> Unit,
    onEpisodeClick: (movie: Episode) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean,
    homeScreenViewModel: ZHomeViewModel = hiltViewModel(),
) {
    val uiState by homeScreenViewModel.uiState.observeAsState(ZHomeScreenUiState.Loading)

    when (uiState) {
        is ZHomeScreenUiState.Ready -> {
            val movies = (uiState as ZHomeScreenUiState.Ready).homeData
            Catalog(
                onClick = onClick,
                movies = movies,
                onSeriesClick = onSeriesClick,
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible,
                modifier = Modifier.fillMaxSize(),
                onEpisodeClick = onEpisodeClick,
            )
        }
        is ZHomeScreenUiState.Loading -> ZHomeScreenUiState.Loading
        is ZHomeScreenUiState.Error -> Error()
        else -> {
            Text(text = "Error")
        }
    }
}

@Composable
private fun Catalog(
    onClick: ()-> Unit,
    movies: List<HomeDatum>,
    onSeriesClick: (movie: Series) -> Unit,
    onEpisodeClick: (movie: Episode) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean,
    modifier: Modifier = Modifier
) {

    val tvLazyListState = rememberTvLazyListState()
    val childPadding = rememberChildPadding()
    val pivotOffset = remember { PivotOffsets() }
    val pivotOffsetForImmersiveList = remember { PivotOffsets(0f, 0f) }
    var immersiveListHasFocus by remember { mutableStateOf(false) }

//    TvLazyColumn {
//        items(movies) { movie ->
//            Text(text = movie.name)
//        }
//    }
    TvLazyColumn(
        modifier = modifier,
        pivotOffsets = if (immersiveListHasFocus) pivotOffsetForImmersiveList else pivotOffset,
        state = tvLazyListState,
        contentPadding = PaddingValues(
            bottom = LocalConfiguration.current.screenHeightDp.dp.times(0.19f)
        )
    ){
        items(movies) { data ->
            if (data.name == "LATEST FROM ARYZAP"){
                SexySeriesCarousel(
                    modifier = Modifier.onFocusChanged {
                        immersiveListHasFocus = it.hasFocus
                    },
                    moviesState = data.data.series!!,
                    onMovieClick = onSeriesClick,
                    titleHeader = data.name.toString()
                )
            }
            if (data.type == "ImageSlider"){
                ZFeaturedSlider(
                    slider = data.data.slider!!.sliderData,
                    padding = childPadding,
                    goToVideoPlayer = onClick)
            }
            if(data.name == "SHOWS"){

                SeriesScreenMovieList(
                    seriesList = data.data.series!!,
                    onMovieClick = onSeriesClick
                )
            }
            if (data.type == "Category" && data.name != "LATEST FROM ARYZAP" && data.name != "SHOWS" ){
                data.data.series?.let {
                    ZNMoviesRow(
                        modifier = Modifier.padding(top = 10.dp).onFocusChanged {
                            immersiveListHasFocus = it.hasFocus
                        },
                        movies = data.data.series,
                        title = data.name,
                        onMovieClick = onSeriesClick
                    )
                }
            }

            if (data.type == "SingleSeries"){
                SingleSeriesCarousel(
                    modifier = Modifier.onFocusChanged {
                        immersiveListHasFocus = it.hasFocus
                    },
                    moviesState = data.data.episode!!,
                    onMovieClick = onEpisodeClick,
                    titleHeader = data.name.toString()
                )
            }
        }
    }
}