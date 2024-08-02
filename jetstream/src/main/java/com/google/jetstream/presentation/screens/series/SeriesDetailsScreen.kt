/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.movies

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceBorder
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.models.Episode
import com.google.jetstream.data.models.Series
import com.google.jetstream.data.models.SeriesSingle
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.common.MoviesRow
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.screens.series.CombineSeriesAndEpisode
import com.google.jetstream.presentation.screens.series.SeriesDetailsScreenUiState
import com.google.jetstream.presentation.screens.series.SeriesDetailsScreenViewModel
import com.google.jetstream.presentation.theme.JetStreamCardShape

object SeriesDetailScreen {
    const val SeriesIdBundleKey = "seriedId"
}

@Composable
fun SeriesDetailScreen(
    detail: String?,
    goToMoviePlayer: (videoPath: String) -> Unit,
    onBackPressed: () -> Unit,
    //refreshScreenWithNewMovie: (Series) -> Unit,
    seriesDetailsScreenViewModel: SeriesDetailsScreenViewModel = hiltViewModel(),
    navController: NavController
) {



    val uiState by seriesDetailsScreenViewModel.uiState.observeAsState(SeriesDetailsScreenUiState.Loading)
    var immersiveListHasFocus by remember { mutableStateOf(false) }

    when (val s = uiState) {
        is SeriesDetailsScreenUiState.Loading -> {
            Loading()
        }
        is SeriesDetailsScreenUiState.Error -> {
            Error()
        }
        is SeriesDetailsScreenUiState.Ready -> {
            SeriesDetail(
                seriesDetails = s.combineSeriesAndEpisode,
                goToMoviePlayer = { goToMoviePlayer(s.combineSeriesAndEpisode.series.seiresCDN) },
                onBackPressed = onBackPressed,
                //refreshScreenWithNewMovie = refreshScreenWithNewMovie,
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                navController = navController
            )
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SeriesDetail(
    seriesDetails: CombineSeriesAndEpisode,
    goToMoviePlayer: (videoPath: String) -> Unit,
    onBackPressed: () -> Unit,
    //refreshScreenWithNewMovie: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val childPadding = rememberChildPadding()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    BackHandler(onBack = onBackPressed)
    TvLazyColumn(
        modifier = modifier
    ) {
        item {
            SeriesDetails(
                seriesDetails = seriesDetails.series,
                episodeDetails = seriesDetails.details,
                goToMoviePlayer = { goToMoviePlayer(seriesDetails.series.seiresCDN) },
                navController = navController
            )
        }
        if(seriesDetails.details.episode.isNotEmpty()){
            item {
                SeriesEpisode(seriesDetails = seriesDetails,
                    modifier = Modifier.padding(
                        top = childPadding.top
                    ),
                    navController = navController
                )
            }

        }

//        item {
//            CastAndCrewList(
//                castAndCrew = seriesDetails.castAndCrew
//            )
//        }

//        item {
//            MoviesRow(
//                title = StringConstants
//                    .Composable
//                    .movieDetailsScreenSimilarTo(seriesDetails.title),
//                titleStyle = MaterialTheme.typography.titleMedium,
//                movies = seriesDetails.similarMovies,
//                onMovieClick = refreshScreenWithNewMovie
//            )
//        }

//        item {
//            MovieReviews(
//                modifier = Modifier.padding(top = childPadding.top),
//                reviewsAndRatings = movieDetails.reviewsAndRatings
//            )
//        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = childPadding.start)
                    .padding(BottomDividerPadding)
                    .fillMaxWidth()
                    .height(1.dp)
                    .alpha(0.15f)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = childPadding.start),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val itemWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.2f)
                val itemModifier = Modifier.width(itemWidth)

                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.status),
                    value = seriesDetails!!.series.status
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.original_language),
                    value = "Urdu / Hindi"
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.budget),
                    value = "NULL"
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.revenue),
                    value = "NULL"
                )
            }
        }

        item {
            Spacer(modifier = Modifier.padding(bottom = screenHeight.times(0.25f)))
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Text(text = "Loading...", modifier = modifier)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Error(modifier: Modifier = Modifier) {
    Text(text = "Something went wrong...", modifier = modifier)
}

private val BottomDividerPadding = PaddingValues(vertical = 48.dp)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun OutlinedCardExample(
    episode: Episode,
    border: ClickableSurfaceBorder,
    modifier: Modifier
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.White),
        modifier = modifier,
    ) {
        Text(
            text = episode.snippet.title,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

private val ReviewItemOutlineWidth = 2.dp