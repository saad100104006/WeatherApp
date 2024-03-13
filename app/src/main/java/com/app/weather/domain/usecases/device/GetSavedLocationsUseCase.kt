package com.app.weather.domain.usecases.device

import com.app.weather.domain.models.FavoriteLocationModel
import javax.inject.Inject

class GetSavedLocationsUseCase @Inject constructor(private val deviceRepository: DeviceRepository) {
    operator fun invoke(): List<FavoriteLocationModel> {
        return deviceRepository.getSavedLocations().map {
            it.toFavoriteLocationModel()
        }
    }

}