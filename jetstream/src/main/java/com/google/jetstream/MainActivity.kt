package com.google.jetstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import com.google.jetstream.data.models.Series
import com.google.jetstream.data.models.SeriesSingle
import com.google.jetstream.presentation.screens.Screens
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.dashboard.DashboardScreen
import com.google.jetstream.presentation.screens.movies.MovieDetailsScreen
import com.google.jetstream.presentation.screens.movies.SeriesDetailScreen
import com.google.jetstream.presentation.screens.series.SDetailsScreen
//import com.google.jetstream.presentation.screens.series.SeriesDetailsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen
import com.google.jetstream.presentation.screens.videoPlayer.ZAPVideoPlayerScreen
import com.google.jetstream.presentation.theme.JetStreamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun MainActivity.App() {
    JetStreamTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) {
                val navController = rememberNavController()
                var isComingBackFromDifferentScreen by remember { mutableStateOf(false) }

                NavHost(
                    navController = navController,
                    startDestination = Screens.Dashboard(),
                    builder = {
                        composable(
                            route = Screens.CategoryMovieList(),
                            arguments = listOf(
                                navArgument(CategoryMovieListScreen.CategoryIdBundleKey) {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            CategoryMovieListScreen(
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                },
                                onMovieSelected = { movie ->
                                    navController.navigate(
                                        Screens.MovieDetails.withArgs(movie.id)
                                    )
                                }
                            )


                        }

                        composable(
                            route = Screens.MovieDetails(),
                            arguments = listOf(
                                navArgument(MovieDetailsScreen.MovieIdBundleKey) {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            MovieDetailsScreen(
                                goToMoviePlayer = {
                                    navController.navigate(Screens.VideoPlayer())
                                },
                                refreshScreenWithNewMovie = { movie ->
                                    navController.navigate(
                                        Screens.MovieDetails.withArgs(movie.id)
                                    ) {
                                        popUpTo(Screens.MovieDetails()) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screens.SeriesDetails(),
                            arguments = listOf(
                                navArgument(SeriesDetailScreen.SeriesIdBundleKey) {
                                    type = NavType.StringType
                                },
                            )
                        ) {
                            val detail = it.arguments?.getString(SeriesDetailScreen.SeriesIdBundleKey)
                            SeriesDetailScreen(
                                detail,
                                goToMoviePlayer = { uripath ->
                                    navController.navigate(Screens.VideoPlayer())
                                },
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                },
                                navController = navController
                            )
                        }

                        composable(route = Screens.Dashboard()) {
                            DashboardScreen(
                                openCategoryMovieList = { categoryId ->
                                    navController.navigate(
                                        Screens.CategoryMovieList.withArgs(categoryId)
                                    )
                                },
                                openMovieDetailsScreen = { movieId ->
                                    navController.navigate(
                                        Screens.MovieDetails.withArgs(movieId)
                                    )
                                },
                                openSeriesDetailScreen = { seriesId ->
                                    navController.navigate(
                                        Screens.SeriesDetails.withArgs(seriesId)
                                    )
                                },
                                openVideoPlayer = {

                                    navController.navigate(
                                        Screens.VideoPlayer()
                                    )
                                },
                                onBackPressed = onBackPressedDispatcher::onBackPressed,
                                isComingBackFromDifferentScreen = isComingBackFromDifferentScreen,
                                resetIsComingBackFromDifferentScreen = {
                                    isComingBackFromDifferentScreen = false
                                },
                            )
                        }
                        composable(
                            route = "${Screens.ZAPVideoPayer.withArgs("uriPath")}",
                            arguments = listOf(
                                navArgument("uripath") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val uripath = it.arguments?.getString("uripath")
                            ZAPVideoPlayerScreen(
                                uripath = uripath,
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                }
                            )
                        }
                        composable(
                            route = Screens.VideoPlayer()
                        ) {

                            VideoPlayerScreen(
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}
