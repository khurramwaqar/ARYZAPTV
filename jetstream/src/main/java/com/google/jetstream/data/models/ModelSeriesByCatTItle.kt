package com.google.jetstream.data.models

import com.google.gson.annotations.SerializedName

data class ModelSeriesByCatTitle (
    val series: List<SeriesN>? = null
)

data class SeriesN (
    @SerializedName("_id") val id: String,
    val title: String? = null,
    val description: String? = null,
    val cast: List<String>? = null,
    val seriesDM: String? = null,
    val seriesYT: String? = null,
    val seiresCDN: String? = null,
    val imagePoster: String? = null,
    val imageCoverMobile: String? = null,
    val imageCoverDesktop: String? = null,
    val trailer: String? = null,
    val ost: String? = null,
    val logo: String? = null,
    val day: String? = null,
    val time: String? = null,
    val status: String? = null,
    val geoPolicy: String? = null,
    val adsManager: String? = null,
    val seriesType: String? = null,
    val publishedAt: String? = null,
    val position: Long? = null,
    val v: Long? = null,
    val isDM: Boolean? = null
)

data class CategoryIdInfo (
    @SerializedName("_id") val id: String,
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val appId: String? = null,
    val v: Long? = null
)

data class GeoPolicyInfo (
    @SerializedName("_id") val id: String,
    val title: String? = null,
    val condition: String? = null,
    val countries: List<String>? = null,
    val v: Long? = null
)