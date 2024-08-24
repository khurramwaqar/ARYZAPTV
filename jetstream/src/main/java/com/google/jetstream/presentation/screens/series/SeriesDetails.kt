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

import Favorites
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.release.aryzaptv.R
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.models.ModelEpisode
import com.google.jetstream.data.models.Series
import com.google.jetstream.data.models.SeriesSingle
import com.google.jetstream.presentation.screens.Screens
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamButtonShape
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun SeriesDetails(
    seriesDetails: SeriesSingle,
    episodeDetails: ModelEpisode,
    goToMoviePlayer: (videoPath: String) -> Unit,
    navController: NavController
) {
    val screenConfiguration = LocalConfiguration.current
    val screenHeight = screenConfiguration.screenHeightDp
    val childPadding = rememberChildPadding()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight.dp.times(0.8f))
    ) {
        SeriesImageWithGradients(
            movieDetails = seriesDetails,
            bringIntoViewRequester = bringIntoViewRequester
        )

        Column(modifier = Modifier.fillMaxWidth(0.55f)) {
            Spacer(modifier = Modifier.height(screenHeight.dp.times(0.2f)))

            Column(
                modifier = Modifier.padding(start = childPadding.start)
            ) {
                SeriesLargeTitle(movieTitle = seriesDetails.title)

                Column(
                    modifier = Modifier.alpha(0.75f)
                ) {
                    SeriesDescription(description = seriesDetails.description)
                    DotSeparatedRow(
                        modifier = Modifier.padding(top = 20.dp),
                        texts = listOf(
                            "PG-13",
                            "2024 - PK",
                            //seriesDetails.categories.joinToString(", "),
                            "Action, Adventute, Love, Romance",
                            "45 Min"
                        )
                    )
                    DirectorScreenplayMusicRow(
                        director = "ARY Digital",
                        screenplay = "ARY Studios",
                        music = "ARY Musik"
                    )
                }


                WatchTrailerButton(
                    modifier = Modifier.onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == NativeKeyEvent.KEYCODE_DPAD_UP) {
                            coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                        }
                        false
                    },
                    goToMoviePlayer = {
                        navController.navigate(Screens.VideoPlayer())
                    }
                )


            }
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun WatchTrailerButton(
    modifier: Modifier = Modifier,
    goToMoviePlayer: () -> Unit
) {
    Button(
        onClick = goToMoviePlayer,
        modifier = modifier.padding(top = 24.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        shape = ButtonDefaults.shape(shape = JetStreamButtonShape)
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.watch_trailer),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun DirectorScreenplayMusicRow(
    director: String,
    screenplay: String,
    music: String
) {
    Row(modifier = Modifier.padding(top = 32.dp)) {
        TitleValueText(
            modifier = Modifier
                .padding(end = 32.dp)
                .weight(1f),
            title = stringResource(R.string.director),
            value = director
        )

        TitleValueText(
            modifier = Modifier
                .padding(end = 32.dp)
                .weight(1f),
            title = stringResource(R.string.screenplay),
            value = screenplay
        )

        TitleValueText(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.music),
            value = music
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SeriesDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier.padding(top = 8.dp),
        maxLines = 2
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SeriesLargeTitle(movieTitle: String) {
    Text(
        text = movieTitle,
        style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        fontSize = 40.sp,
        maxLines = 2
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalTvMaterial3Api::class)
@Composable
private fun SeriesImageWithGradients(
    movieDetails: SeriesSingle,
    bringIntoViewRequester: BringIntoViewRequester
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data("https://node.aryzap.com/public/"+movieDetails.imageCoverDesktop)
            .crossfade(true).build(),
        contentDescription = StringConstants
            .Composable
            .ContentDescription
            .moviePoster(movieDetails.title),
        modifier = Modifier
            .fillMaxSize()
            .bringIntoViewRequester(bringIntoViewRequester),
        contentScale = ContentScale.FillWidth
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface
                    ),
                    startY = 600f
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        Color.Transparent
                    ),
                    endX = 1000f,
                    startX = 300f
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        Color.Transparent
                    ),
                    start = Offset(x = 500f, y = 500f),
                    end = Offset(x = 1000f, y = 0f)
                )
            )
    )
}
