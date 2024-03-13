package com.app.weather.domain.usecases.weather

import com.app.weather.domain.Result
import com.app.weather.domain.models.SearchResultModel
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: String?): Result<List<SearchResultModel>> {
        return weatherRepository.search(city?:"")
    }
}