package com.app.weather.data.repositories

import com.app.weather.data.source.weather.WeatherDataSource
import com.app.weather.domain.Result
import com.app.weather.domain.models.SearchResultModel
import com.app.weather.domain.models.WeatherData
import com.app.weather.domain.usecases.weather.WeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(private val weatherDataSource: WeatherDataSource) :
    WeatherRepository {
    override suspend fun fetchWeatherData(city: String): Result<WeatherData.Daily> {
        return weatherDataSource.getCurrentWeather(city)
    }

    override suspend fun fetchHourlyWeatherData(
        city: String,
        noOfDays: Int
    ): Result<List<WeatherData.Daily>> {
        return weatherDataSource.getHourlyForecastData(city, noOfDays)
    }

    override suspend fun search(city: String): Result<List<SearchResultModel>> {
        return weatherDataSource.search(city)
    }
}