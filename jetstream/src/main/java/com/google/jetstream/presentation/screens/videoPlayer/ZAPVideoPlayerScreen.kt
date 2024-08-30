package com.google.jetstream.presentation.screens.videoPlayer

import android.net.Uri
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.google.gson.Gson
import com.google.jetstream.data.models.SeriesN
import com.google.jetstream.presentation.screens.series.SeriesDetailsScreenUiState
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerControls
import com.google.jetstream.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import com.google.jetstream.presentation.utils.handleDPadKeyEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
object ZAPVideoPlayerScreen {
    const val ZPlayerBundleId = "seriesId"
}
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ZAPVideoPlayerScreen(
    mediaUri: Uri = Uri.parse("https://vod.aryzap.com/be8170a0vodtranssgp1313565080/369a456d3270835009874892463/adp.1443317.m3u8?bkreg=IND"),
    onBackPressed: () -> Unit,
    zapVideoPlayerScreenViewModel: ZAPVideoPlayerScreenViewModel = hiltViewModel()
) {
    val uiState by zapVideoPlayerScreenViewModel.uiState.observeAsState(ZAPVideoPlayerScreenUiState.Loading)
    when (val s = uiState) {
        is ZAPVideoPlayerScreenUiState.Loading -> {
           Loading()
        }
        is ZAPVideoPlayerScreenUiState.Error -> {
            Error()
        }
        is ZAPVideoPlayerScreenUiState.Ready -> {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            var contentCurrentPosition: Long by remember { mutableStateOf(0L) }
            val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)
            //val series = Gson().fromJson(seriesJson, SeriesN::class.java)
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                    val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                        context,
                        defaultDataSourceFactory
                    )
                    val source = HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(s.series.seiresCDN.toString()))

                    setMediaSource(source)
                    prepare()
                    //Log.d("dataVC", seriesJson.toString())
                }
            }

            BackHandler(onBack = onBackPressed)

            LaunchedEffect(Unit) {
                while (true) {
                    delay(300)
                    contentCurrentPosition = exoPlayer.currentPosition
                }
            }

            LaunchedEffect(Unit) {
                with(exoPlayer) {
                    playWhenReady = true
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                    repeatMode = Player.REPEAT_MODE_ONE
                }
            }

            Box {
                DisposableEffect(
                    AndroidView(
                        modifier = Modifier
                            .handleDPadKeyEvents(
                                onEnter = {
                                    if (!videoPlayerState.isDisplayed) {
                                        coroutineScope.launch {
                                            videoPlayerState.showControls()
                                        }
                                    }
                                }
                            )
                            .focusable(),
                        factory = {
                            PlayerView(context).apply {
                                hideController()
                                useController = false
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                                player = exoPlayer
                                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                            }
                        }
                    )
                ) {
                    onDispose {
                        exoPlayer.release()
                    }
                }
                VideoPlayerControls(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    isPlaying = exoPlayer.isPlaying,
                    onPlayPauseToggle = { shouldPlay ->
                        if (shouldPlay) {
                            exoPlayer.play()
                        } else {
                            exoPlayer.pause()
                        }
                    },
                    contentProgressInMillis = contentCurrentPosition,
                    contentDurationInMillis = exoPlayer.duration,
                    state = videoPlayerState,
                    onSeek = { seekProgress ->
                        exoPlayer.seekTo(exoPlayer.duration.times(seekProgress).toLong())
                    }
                )
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
    Text(text = "Something went wrong...", modifier = modifier)
}
