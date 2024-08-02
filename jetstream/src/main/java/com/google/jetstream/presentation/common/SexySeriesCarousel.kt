package com.google.jetstream.presentation.common
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveList
import androidx.tv.material3.ImmersiveListScope
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.R
import com.google.jetstream.data.models.Episode
import com.google.jetstream.data.models.Series
import com.google.jetstream.presentation.common.ItemDirection
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SexySeriesCarousel(
    modifier: Modifier = Modifier,
    moviesState: List<Series>,
    onMovieClick: (movie: Series) -> Unit,
    titleHeader: String

) {
    var currentItemIndex by remember { mutableStateOf(0) }
    var isListFocused by remember { mutableStateOf(false) }
    var currentYCoord: Float? by remember { mutableStateOf(null) }

    ImmersiveList(
        modifier = modifier.onGloballyPositioned { currentYCoord = it.positionInWindow().y },
        background = { _, listHasFocus ->
            isListFocused = listHasFocus
            val gradientColor = MaterialTheme.colorScheme.surface
            AnimatedVisibility(
                visible = isListFocused,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier
                    .height(LocalConfiguration.current.screenHeightDp.times(0.8f).dp)
                    .gradientOverlays(gradientColor)
            ) {
                val movie = remember(moviesState, currentItemIndex) {
                    moviesState[currentItemIndex]
                }

                Crossfade(
                    targetState = "https://node.aryzap.com/public/"+ movie.imageCoverDesktop,
                    label = "posterUriCrossfade"
                ) { posterUri ->
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                LocalConfiguration.current.screenHeightDp.times(0.8f).dp
                            )
                        ,
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(posterUri)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

            }
        },
        list = {
            Column {
                // TODO this causes the whole vertical list to jump
                if (isListFocused) {
                    val movie = remember(moviesState, currentItemIndex) {
                        moviesState[currentItemIndex]
                    }
                    Column(
                        modifier = Modifier.padding(
                            start = rememberChildPadding().start,
                            bottom = 32.dp
                        )
                    ) {
                        if(movie.logo.isNotEmpty()){
                            AsyncImage(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(60.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://node.aryzap.com/public/"+movie.logo)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Fit
                            )
                        }else{
                            Text(text = movie.title, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.padding(top = 8.dp))
//                        Chip(onClick = { /*TODO*/ }) {
//                            Text(text = "DRAMA")
//                        }
//                        Chip(onClick = { /*TODO*/ }) {
//                            Text(text = "All Ages")
//                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            text = movie.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                ImmersiveListMoviesRowSSC(
                    itemWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.12f),
                    itemDirection = ItemDirection.Vertical,
                    movies = moviesState,
                    title = titleHeader,
                    showItemTitle = !isListFocused,
                    onMovieClick = onMovieClick,
                    showIndexOverImage = true,
                    focusedItemIndex = { focusedIndex -> currentItemIndex = focusedIndex }
                )
            }
        }
    )
}

fun Modifier.gradientOverlays(gradientColor: Color) = this then drawWithCache {
    val horizontalGradient = Brush.horizontalGradient(
        colors = listOf(
            gradientColor,
            Color.Transparent
        ),
        startX = size.width.times(0.2f),
        endX = size.width.times(0.7f)
    )
    val verticalGradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            gradientColor
        ),
        endY = size.width.times(0.3f)
    )
    val linearGradient = Brush.linearGradient(
        colors = listOf(
            gradientColor,
            Color.Transparent
        ),
        start = Offset(
            size.width.times(0.2f),
            size.height.times(0.5f)
        ),
        end = Offset(
            size.width.times(0.9f),
            0f
        )
    )

    onDrawWithContent {
        drawContent()
        drawRect(horizontalGradient)
        drawRect(verticalGradient)
        drawRect(linearGradient)
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveListScope.ImmersiveListMoviesRowSSC(
    modifier: Modifier = Modifier,
    itemWidth: Dp = LocalConfiguration.current.screenWidthDp.dp.times(0.165f),
    itemDirection: ItemDirection = ItemDirection.Vertical,
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
    movies: List<Series>,
    onMovieClick: (movie: Series) -> Unit = {}
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
                            MoviesRowItemSSC(
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
private fun MoviesRowItemSSC(
    modifier: Modifier = Modifier,
    focusedItemIndex: (index: Int) -> Unit,
    index: Int,
    itemWidth: Dp,
    itemDirection: ItemDirection,
    onMovieClick: (movie: Series) -> Unit,
    movie: Series,
    showItemTitle: Boolean,
    showIndexOverImage: Boolean
) {
    var isItemFocused by remember { mutableStateOf(false) }

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
            MoviesRowItemTextSSC(
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
                MoviesRowItemImageSSC(
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
private fun MoviesRowItemImageSSC(
    modifier: Modifier = Modifier,
    showIndexOverImage: Boolean,
    movie: Series,
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
                .data("https://node.aryzap.com/public/"+movie.imagePoster)
                .build(),
            contentDescription = "movie poster of ${movie.title}",
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
                        color = Color.White,
                        fontSize = 25.sp
                    ),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun MoviesRowItemTextSSC(
    showItemTitle: Boolean,
    isItemFocused: Boolean,
    movie: Series
) {
    if (showItemTitle) {
        val movieNameAlpha by animateFloatAsState(
            targetValue = if (isItemFocused) 1f else 0f,
            label = "",
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
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
