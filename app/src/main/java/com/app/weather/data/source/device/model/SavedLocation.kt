package com.app.weather.data.source.device.model

import com.app.weather.domain.models.FavoriteLocationModel

data class SavedLocation(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val isDefault: Boolean = false
) {
    fun toFavoriteLocationModel(): FavoriteLocationModel {
        return FavoriteLocationModel(
            id = id,
            name = name,
            region = region,
            country = country,
            isDefault = isDefault,
        )
    }
}