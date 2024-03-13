package com.app.weather.data.source.weather

import com.app.weather.data.source.weather.models.response.CurrentWeatherApiResponse
import com.app.weather.data.source.weather.models.response.DailyForecastApiResponse
import com.app.weather.data.source.weather.models.response.SearchApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeatherData(@Query("q") city: String): Response<CurrentWeatherApiResponse>

    @GET("forecast.json")
    suspend fun getHourlyForecastData(@Query("q") city: String, @Query("days") days: Int) : Response<DailyForecastApiResponse>

    @GET("search.json")
    suspend fun searchCity(@Query("q") city: String): Response<List<SearchApiResponse>>
}