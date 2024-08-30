package com.google.jetstream

import Favorites
import LoginScreen
import SignupScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.jetstream.presentation.screens.Screens
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.categories.ZCategoryMovieListScreen
import com.google.jetstream.presentation.screens.dashboard.DashboardScreen
import com.google.jetstream.presentation.screens.movies.MovieDetailsScreen
import com.google.jetstream.presentation.screens.movies.SeriesDetailScreen
//import com.google.jetstream.presentation.screens.series.SeriesDetailsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen
import com.google.jetstream.presentation.screens.videoPlayer.YouTubeWebViewScreen
import com.google.jetstream.presentation.screens.videoPlayer.ZAPVideoPlayerScreen
import com.google.jetstream.presentation.theme.JetStreamTheme
import dagger.hilt.android.AndroidEntryPoint
import handleGoogleSignInResult
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this@MainActivity)
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        val config = RealmConfiguration.Builder(
            schema = setOf(Favorites::class)
        ).name("myFavDb.realm").build()
        Realm.open(config)
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
                    startDestination = Screens.Login(),
                    builder = {
                        composable(
                            route = Screens.CategoryMovieList(),
                            arguments = listOf(
                                navArgument(CategoryMovieListScreen.CategoryIdBundleKey) {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            ZCategoryMovieListScreen(
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                },
                                onMovieSelected = { series ->
                                    navController.navigate(
                                        Screens.SeriesDetails.withArgs(series.id)
                                    )
                                },
                                onLiveSelected = { series ->
                                    navController.navigate(
                                        Screens.ZAPVideoPayer.withArgs(series.id)
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
                            route = Screens.ZAPVideoPayer(),
                            arguments = listOf(
                                navArgument(ZAPVideoPlayerScreen.ZPlayerBundleId) {
                                    type = NavType.StringType
                                    nullable = true
                                },
                            )

                        ) {
                            val detail = it.arguments?.getString(ZAPVideoPlayerScreen.ZPlayerBundleId)
                            ZAPVideoPlayerScreen(
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                }
                            )
                        }
                        composable(
                            route = Screens.VideoPlayer(),
                            arguments = listOf(
                                navArgument("seriesJson") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) {
                            val seriesJson2 = it.arguments?.getString("seriesJson")
                            VideoPlayerScreen(
                                seriesJson = seriesJson2,
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                }
                            )
                        }
                        composable(route = Screens.Login()) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(Screens.Dashboard()) {
                                        popUpTo(Screens.Login()) { inclusive = true }
                                    }
                                },
                                onNavigateToSignup = {
                                    navController.navigate(Screens.Signup())
                                },
                                appContext = applicationContext
                            )
                        }
                        composable(route = Screens.Signup()) {
                            SignupScreen(
                                onSignupSuccess = {
                                    navController.navigate(Screens.Dashboard()) {
                                        popUpTo(Screens.Signup()) { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.navigate(Screens.Login())
                                },
                                appContext = applicationContext
                            )
                        }
                        composable(
                            route = Screens.YTWebView(),
                            arguments = listOf(
                                navArgument("videoId") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val videoId = it.arguments?.getString("videoId") ?: ""
                            YouTubeWebViewScreen(videoId = videoId)
                        }

                    }
                )
            }
        }
    }
}
