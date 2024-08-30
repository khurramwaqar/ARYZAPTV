package com.google.jetstream.data.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName

data class ZapHome (
    val home: Home,
    val isCached: Boolean = false
)

data class Home (
    val id: String,
    val homeTitle: String,
    val homeAppId: List<String>,
    val homeData: List<HomeDatum>
)

data class HomeDatum (
    val id: Long,
    val name: String,
    val type: String,
    val items: Long,
    val data: HomeDatumData,
    val chosen: Boolean,
    val selected: Boolean
)

data class HomeDatumData (
    val slider: Slider? = null,
    val episode: List<Episode>? = null,
    val series: List<Series>? = null,
    val countryCode: String? = null
)

data class Episode (
    @SerializedName("_id")
    val id: String,
    val seriesId: String,
    val videoSource: String? = null,
    val title: String,
    val description: String,
    val imagePath: String,
    val videoYtId: String? = null,
    val videoDmId: String? = null,
    val videoViews: String? = null,
    val videoLength: String? = null,
    @SerializedName("__v")
    val v: Long
)

data class ContentDetails (
    val videoId: String,
    val videoPublishedAt: String
)

data class Snippet (
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    val playlistId: String,
    val position: Long,
    val resourceId: ResourceId,
    val videoOwnerChannelTitle: String,
    val videoOwnerChannelId: String
)

data class ResourceId (
    val kind: String,
    val videoId: String
)

data class Thumbnails (
    val default: Default,
    val medium: Default,
    val high: Default,
    val standard: Default,
    val maxres: Default
)

data class Default (
    val url: String,
    val width: Long,
    val height: Long
)

data class Status (
    val privacyStatus: String
)

data class Series (
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
    val imageCoverDesktop: String,
    val trailer: String,
    val ost: String,
    val logo: String,
    val day: String,
    val time: String,
    val ageRatingId: String,
    val genreId: List<String>,
    val categoryId: List<String>,
    val appId: List<String>,
    val status: String,
    val geoPolicy: String,
    val adsManager: String,
    val seriesType: String,
    val v: Long,
    val publishedAt: String? = null,
    val position: Long,
    val categoryIdInfo: List<CategoryidInfo>,
    val geoPolicyInfo: List<GeoPolicy>
)

data class CategoryidInfo (
    @SerializedName("_id")
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val appId: String,
    val v: Long
)

data class GeoPolicy (
    @SerializedName("_id")
    val id: String,
    val title: String,
    val condition: String,
    val countries: List<String>,
    val v: Long
)

data class Slider (
    @SerializedName("_id")
    val id: String,
    val sliderTitle: String,
    val sliderAppId: List<String>,
    val sliderData: List<SliderDatum>,
    val v: Long
)

data class SliderDatum (

    val id: Long,
    val name: String,
    val type: String,
    val items: String,
    val imagePath: String,
    val chosen: Boolean,
    val selected: Boolean,
    val title: String
)

sealed class DataUnion {
    class DataDataValue(val value: DataData) : DataUnion()
    class StringValue(val value: String)     : DataUnion()
}

data class DataData (
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
    val imageCoverDesktop: String,
    val trailer: String,
    val ost: String,
    val logo: String,
    val day: String,
    val time: String,
    val ageRatingId: CategoryidInfo,
    val genreId: List<CategoryidInfo>,
    val categoryId: List<CategoryidInfo>,
    val appId: List<AppId>,
    val status: String,
    val geoPolicy: GeoPolicy,
    val adsManager: String,
    val v: Long,
    val seriesType: String? = null
)

data class AppId (
    @SerializedName("_id")
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val bundleId: String,
    val platform: String,
    val v: Long
)
