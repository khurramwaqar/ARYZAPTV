package com.google.jetstream.data.models

import com.google.gson.annotations.SerializedName


data class SeriesSingle (
    @SerializedName("_id")
    val id: String,
    val title: String,
    val description: String,
    val cast: List<String>,
    val seriesDM: String,
    val seriesYT: String,
    val seiresCDN: String,
    val imagePoster: String,
    val imageCoverMobile: String,
    val trailer: String,
    val ost: String,
    val logo: String,
    val day: String,
    val time: String,
    val ageRatingId: ID,
    val genreId: List<ID>,
    val categoryId: List<ID>,
    val appId: List<AppIdMS>,
    val status: String,
    val v: Long,
    val geoPolicy: GeoPolicyMS,
    val adsManager: String,
    val imageCoverDesktop: String,
    val seriesType: String,
    val publishedAt: String,
    val position: Long
)

data class ID (
    @SerializedName("_id")
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val appId: String,
    val v: Long
)

data class AppIdMS (
    @SerializedName("_id")
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val bundleId: String,
    val platform: String,
    val v: Long
)

data class GeoPolicyMS (
    @SerializedName("_id")
    val id: String,
    val title: String,
    val condition: String,
    val countries: List<String>,
    val v: Long
)
