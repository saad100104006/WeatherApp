package com.app.weather.domain.usecases.weather

import com.app.weather.domain.Result
import com.app.weather.domain.models.WeatherData
import javax.inject.Inject

class FetchCurrentWeatherDataUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: String): Result<WeatherData.Daily> {
        return weatherRepository.fetchWeatherData(city)

    }

}