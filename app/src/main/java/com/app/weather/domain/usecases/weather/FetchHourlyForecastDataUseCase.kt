package com.app.weather.domain.usecases.weather

import com.app.weather.domain.Result
import com.app.weather.domain.models.WeatherData
import javax.inject.Inject

class FetchHourlyForecastDataUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: String, noOfDays: Int): Result<List<WeatherData.Daily>> {
        return weatherRepository.fetchHourlyWeatherData(city, noOfDays)

    }
}