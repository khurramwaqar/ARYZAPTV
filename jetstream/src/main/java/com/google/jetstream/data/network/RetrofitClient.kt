package com.google.jetstream.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRetrofit(): Retrofit {
    val gson = GsonBuilder().create()
    return Retrofit.Builder()
        .baseUrl("https://node.aryzap.com/api/") // Replace with your API base URL
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}