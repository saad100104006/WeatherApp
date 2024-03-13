package com.app.weather.data.source.weather

import android.util.Log
import com.app.weather.domain.Result
import com.app.weather.domain.models.SearchResultModel
import com.app.weather.domain.models.WeatherData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherDataSource @Inject constructor() {
    companion object {
        const val API_BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = "0a34578cf470486ab9d64647241303"
    }

    private val weatherApiService: WeatherApiService
    private val httpClient: OkHttpClient

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        httpClient = okHttpClientBuilder.addNetworkInterceptor(Interceptor { chain ->
            with(chain.request()) {
                chain.proceed(
                    this.newBuilder().url(
                        this.url.newBuilder()
                            .addQueryParameter("key", API_KEY).build()
                    ).build()
                )
            }
        }).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApiService = retrofit.create(WeatherApiService::class.java)
    }

    suspend fun getCurrentWeather(city: String): Result<WeatherData.Daily> {
        val response = weatherApiService.getCurrentWeatherData(city)
        Log.e("API Response", response.body().toString())
        if (response.isSuccessful) {
            response.body()?.toWeatherData()?.let {
                return Result.Success(it)
            }
        } else {
            return Result.Error(Exception(response.errorBody().toString()))
        }
        return Result.Error(Exception("Unknown error"))
    }

    suspend fun getHourlyForecastData(
        city: String,
        noOfDays: Int
    ): Result<List<WeatherData.Daily>> {
        val response = weatherApiService.getHourlyForecastData(city, noOfDays)
        Log.e("API Response", response.body().toString())
        if (response.isSuccessful) {
            response.body()?.toForecastData()?.let {
                return Result.Success(it)
            }
        } else {
            return Result.Error(Exception(response.errorBody().toString()))
        }
        return Result.Error(Exception("Unknown error"))
    }

    suspend fun search(city: String): Result<List<SearchResultModel>> {
        val response = weatherApiService.searchCity(city)
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Result.Success(responseBody.map { item ->
                    item.toSearchResultModel()
                })
            }

        } else {
            return Result.Error(Exception(response.errorBody().toString()))
        }
        return Result.Error(Exception("Unknown error"))
    }
}