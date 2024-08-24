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

package com.google.jetstream.presentation.screens.categories

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieCategoryDetails
import com.google.jetstream.data.models.ModelSeriesByCatTitle
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamBottomListPadding
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.presentation.utils.focusOnInitialVisibility

object ZCategoryMovieListScreen {
    const val CategoryIdBundleKey = "categoryId"
}

@Composable
fun ZCategoryMovieListScreen(
    onBackPressed: () -> Unit,
    onMovieSelected: (Movie) -> Unit,
    categoryMovieListScreenViewModel: ZCategoryMovieListScreenViewModel = hiltViewModel()
) {
    val uiState by categoryMovieListScreenViewModel.uiState.observeAsState(ZCategoryMovieListScreenUiState.Loading)

    when (val s = uiState) {
        is ZCategoryMovieListScreenUiState.Loading -> {
            Loading()
        }
        is ZCategoryMovieListScreenUiState.Error -> {
            Error()
        }
        is ZCategoryMovieListScreenUiState.Done -> {
            val categoryDetails = s.seriesByCatTitle
            val title = s.title
            CategoryDetails(
                mainTitle = title,
                categoryDetails = categoryDetails,
                onBackPressed = onBackPressed,
                onMovieSelected = onMovieSelected
            )
        }
    }

}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CategoryDetails(
    mainTitle: String,
    categoryDetails: ModelSeriesByCatTitle,
    onBackPressed: () -> Unit,
    onMovieSelected: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val childPadding = rememberChildPadding()
    val isFirstItemVisible = remember { mutableStateOf(false) }

    BackHandler(onBack = onBackPressed)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = mainTitle,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(
                vertical = childPadding.top.times(3.5f)
            )
        )
        TvLazyVerticalGrid(
            columns = TvGridCells.Fixed(6)
        ) {
            categoryDetails.series?.forEachIndexed { index, movie ->
                item {
                    key(movie.id) {
                        StandardCardLayout(
                            modifier = Modifier
                                .aspectRatio(1 / 1.5f)
                                .padding(8.dp)
                                .then(
                                    if (index == 0)
                                        Modifier.focusOnInitialVisibility(isFirstItemVisible)
                                    else Modifier
                                ),
                            imageCard = {
                                CardLayoutDefaults.ImageCard(
                                    shape = CardDefaults.shape(shape = JetStreamCardShape),
                                    border = CardDefaults.border(
                                        focusedBorder = Border(
                                            border = BorderStroke(
                                                width = JetStreamBorderWidth,
                                                color = MaterialTheme.colorScheme.onSurface
                                            ),
                                            shape = JetStreamCardShape
                                        ),
                                        pressedBorder = Border(
                                            border = BorderStroke(
                                                width = JetStreamBorderWidth,
                                                color = MaterialTheme.colorScheme.border
                                            ),
                                            shape = JetStreamCardShape
                                        ),
                                    ),
                                    scale = CardDefaults.scale(focusedScale = 1f),
                                    onClick = { Log.d("logDataScreen", movie.imagePoster.toString()) },
                                    interactionSource = it
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data("https://node.aryzap.com/public/"+movie.imagePoster)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = StringConstants
                                            .Composable
                                            .ContentDescription
                                            .moviePoster(movie.title.toString()),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            },
                            title = {},
                        )
                    }
                }
            }
            item(span = { TvGridItemSpan(currentLineSpan = 6) }) {
                Spacer(modifier = Modifier.padding(bottom = JetStreamBottomListPadding))
            }
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
    Text(text = "Wops, something went wrong...", modifier = modifier)
}