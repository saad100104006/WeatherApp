package com.app.weather.data.source.weather.models

import com.google.gson.annotations.SerializedName

data class Location(
    val name:String,
    val region: String,
    val country: String,
    @SerializedName("localtime")
    val localTime: String,
    @SerializedName("localtime_epoch")
    val localTimeEpoch: Long
)