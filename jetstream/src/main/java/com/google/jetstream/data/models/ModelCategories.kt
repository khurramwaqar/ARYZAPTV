package com.google.jetstream.data.models

import com.google.gson.annotations.SerializedName


data class ModelCategories (
    @SerializedName("_id") val id: String,
    val title: String,
    val description: String,
    val image: String,
    val appId: String,
    @SerializedName("__v") val version: Int
)
