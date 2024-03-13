package com.app.weather.infrastructure.util

import com.google.gson.Gson
import com.app.weather.domain.models.FavoriteLocationModel

fun String.toFavoriteLocationModel(): FavoriteLocationModel {
    val converter = Gson()
    return converter.fromJson(this, FavoriteLocationModel::class.java)
}