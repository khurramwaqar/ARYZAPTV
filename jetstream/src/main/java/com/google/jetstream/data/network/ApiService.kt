package com.google.jetstream.data.network

import com.google.jetstream.data.models.ModelCategories
import com.google.jetstream.data.models.ModelEpisode
import com.google.jetstream.data.models.ModelSeriesByCatTitle
import com.google.jetstream.data.models.SeriesSingle
import com.google.jetstream.data.models.ZapHome
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    // Define API endpoints with corresponding HTTP methods and parameters
    @GET("homev2/669f61033fe1bf91a6a4e274/PK") // Replace with your actual API endpoint
    suspend fun getHomeData(): ZapHome

    @GET("categories") // Replace with your actual API endpoint
    suspend fun getCategories(): ArrayList<ModelCategories>

    @GET("series/{id}") // Replace with your actual API endpoint
    suspend fun getSingleSeries(@Path("id") id: String): SeriesSingle

    @GET("series/byCatID/{id}") // Replace with your actual API endpoint
    suspend fun getSeriesByCatName(@Path("id") id: String): ModelSeriesByCatTitle

    @GET("yt/{id}") // Replace with your actual API endpoint
    suspend fun getEpisodeBySeriesId(@Path("id") id: String): ModelEpisode
}

// Create a Retrofit instance
object RetrofitClient {

    private const val BASE_URL = "https://node.aryzap.com/api/"
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}
