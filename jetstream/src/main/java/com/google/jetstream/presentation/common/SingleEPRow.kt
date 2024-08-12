package com.google.jetstream.presentation.common


import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveListScope
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.models.Episode
import com.google.jetstream.data.models.Series
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.presentation.utils.createInitialFocusRestorerModifiers
import com.google.jetstream.presentation.utils.ifElse
import java.util.UUID

enum class ItemDirectionSEPROW(val aspectRatio: Float) {
    Vertical(20.5f / 12f),
    Horizontal(20f / 11f);
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SingleEPRow(
    modifier: Modifier = Modifier,
    itemWidth: Dp = LocalConfiguration.current.screenWidthDp.dp.times(0.275f),
    itemDirection: ItemDirectionSEPROW = ItemDirectionSEPROW.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    focusedItemIndex: (index: Int) -> Unit = {},
    movies: List<Episode>,
    onMovieClick: (movie: Episode) -> Unit = {}
) {
    Column(
        modifier = modifier.focusGroup()
    ) {
        title?.let { nnTitle ->
            Text(
                text = nnTitle,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(start = startPadding)
                    .padding(vertical = 16.dp)
            )
        }

        AnimatedContent(
            targetState = movies,
            label = "",
        ) { movieState ->
            val focusRestorerModifiers = createInitialFocusRestorerModifiers()

            TvLazyRow(
                modifier = Modifier
                    .then(focusRestorerModifiers.parentModifier),
                pivotOffsets = PivotOffsets(parentFraction = 0.07f),
                contentPadding = PaddingValues(
                    start = startPadding,
                    end = endPadding,
                ),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                itemsIndexed(movieState, key = { _, movie -> movie.id }) { index, movie ->
                    SingleEPRowItem(
                        modifier = Modifier.ifElse(
                            index == 0,
                            focusRestorerModifiers.childModifier
                        ),
                        focusedItemIndex = focusedItemIndex,
                        index = index,
                        itemWidth = itemWidth,
                        itemDirection = itemDirection,
                        onMovieClick = onMovieClick,
                        movie = movie,
                        showItemTitle = showItemTitle,
                        showIndexOverImage = showIndexOverImage
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveListScope.ImmersiveListMoviesRow(
    modifier: Modifier = Modifier,
    itemWidth: Dp = LocalConfiguration.current.screenWidthDp.dp.times(0.165f),
    itemDirection: ItemDirectionSEPROW = ItemDirectionSEPROW.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    focusedItemIndex: (index: Int) -> Unit = {},
    movies: List<Episode>,
    onMovieClick: (movie: Episode) -> Unit = {}
) {
    Column(
        modifier = modifier.focusGroup()
    ) {
        title?.let { nnTitle ->
            Text(
                text = nnTitle,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(start = startPadding)
                    .padding(vertical = 16.dp)
            )
        }

        AnimatedContent(
            targetState = movies,
            label = "",
        ) { movieState ->
            TvLazyRow(
                modifier = Modifier.focusRestorer(),
                pivotOffsets = PivotOffsets(parentFraction = 0.07f)
            ) {
                item { Spacer(modifier = Modifier.padding(start = startPadding)) }

                movieState.forEachIndexed { index, movie ->
                    item {
                        key(movie.id) {
                            SingleEPRowItem(
                                modifier = Modifier
                                    .immersiveListItem(index),
                                focusedItemIndex = focusedItemIndex,
                                index = index,
                                itemWidth = itemWidth,
                                itemDirection = itemDirection,
                                onMovieClick = onMovieClick,
                                movie = movie,
                                showItemTitle = showItemTitle,
                                showIndexOverImage = showIndexOverImage
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.padding(end = 20.dp)) }
                }

                item { Spacer(modifier = Modifier.padding(start = endPadding)) }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
private fun SingleEPRowItem(
    modifier: Modifier = Modifier,
    focusedItemIndex: (index: Int) -> Unit,
    index: Int,
    itemWidth: Dp,
    itemDirection: ItemDirectionSEPROW,
    onMovieClick: (movie: Episode) -> Unit,
    movie: Episode,
    showItemTitle: Boolean,
    showIndexOverImage: Boolean
) {
    var isItemFocused by remember { mutableStateOf(false) }

    val context = LocalContext.current
    StandardCardLayout(
        modifier = Modifier
            .width(itemWidth)
            .onFocusChanged {
                isItemFocused = it.isFocused
                if (isItemFocused) {
                    focusedItemIndex(index)
                }
            }
            .focusProperties {
                if (index == 0) {
                    left = FocusRequester.Cancel
                }
            }
            .then(modifier),
        title = {
            SingleEPRowItemText(
                showItemTitle = showItemTitle,
                isItemFocused = isItemFocused,
                movie = movie
            )
        },
        imageCard = {
            CardLayoutDefaults.ImageCard(
                onClick = { onMovieClick(movie) },
                shape = CardDefaults.shape(JetStreamCardShape),
                border = CardDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = JetStreamBorderWidth,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = JetStreamCardShape
                    )
                ),
                scale = CardDefaults.scale(focusedScale = 1f),
                interactionSource = it
            ) {
                SingleEPRowItemImage(
                    modifier = Modifier.aspectRatio(itemDirection.aspectRatio),
                    showIndexOverImage = showIndexOverImage,
                    movie = movie,
                    index = index
                )
            }
        },
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SingleEPRowItemImage(
    modifier: Modifier = Modifier,
    showIndexOverImage: Boolean,
    movie: Episode,
    index: Int
) {
    Box(contentAlignment = Alignment.CenterStart) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    if (showIndexOverImage) {
                        drawRect(
                            color = Color.Black.copy(
                                alpha = 0.1f
                            )
                        )
                    }
                },
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(movie.snippet.thumbnails.medium.url)
                .build(),
            contentDescription = "movie poster of ${movie.snippet.title}",
            contentScale = ContentScale.Crop
        )
        if (showIndexOverImage) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "#${index.inc()}",
                style = MaterialTheme.typography.displayLarge
                    .copy(
                        shadow = Shadow(
                            offset = Offset(0.5f, 0.5f),
                            blurRadius = 5f
                        ),
                        color = Color.White
                    ),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SingleEPRowItemText(
    showItemTitle: Boolean,
    isItemFocused: Boolean,
    movie: Episode
) {
    if (showItemTitle) {
        val movieNameAlpha by animateFloatAsState(
            targetValue = if (isItemFocused) 1f else 0f,
            label = "",
        )
        Text(
            text = movie.snippet.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .alpha(movieNameAlpha)
                .fillMaxWidth()
                .padding(top = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
