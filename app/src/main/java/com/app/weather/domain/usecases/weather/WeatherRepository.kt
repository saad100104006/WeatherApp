package com.app.weather.domain.usecases.weather

import com.app.weather.domain.Result
import com.app.weather.domain.models.SearchResultModel
import com.app.weather.domain.models.WeatherData

interface WeatherRepository {
    suspend fun fetchWeatherData(city: String): Result<WeatherData.Daily>
    suspend fun fetchHourlyWeatherData(city: String, noOfDays:Int): Result<List<WeatherData.Daily>>
    suspend fun search(city:String): Result<List<SearchResultModel>>
}