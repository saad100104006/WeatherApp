package com.app.weather.domain.models

import com.google.gson.Gson
import com.app.weather.data.source.device.model.SavedLocation
import com.app.weather.presentation.screen.home.model.WeatherData

data class FavoriteLocationModel(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val isDefault: Boolean = false,
    val weatherData: WeatherData? = null
) {
    fun getDisplayName(): String {
        return "$name,\n$country"
    }

    fun getIdentifier(): String {
        return "$name $region $country"
    }

    fun toSavedLocationModel(): SavedLocation {
        return SavedLocation(
            id = id,
            name = name,
            region = region,
            country = country,
            isDefault = isDefault
        )
    }

     fun toStringArg(): String {
        return Gson().toJson(this)
    }
}