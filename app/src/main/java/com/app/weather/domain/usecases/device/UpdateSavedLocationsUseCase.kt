package com.app.weather.domain.usecases.device

import com.app.weather.domain.models.FavoriteLocationModel
import javax.inject.Inject

class UpdateSavedLocationsUseCase @Inject constructor(private val deviceRepository: DeviceRepository) {
    operator fun invoke(favoriteLocations: List<FavoriteLocationModel>) {
        return deviceRepository.updateSavedLocations(favoriteLocations.map { it.toSavedLocationModel() })
    }
}