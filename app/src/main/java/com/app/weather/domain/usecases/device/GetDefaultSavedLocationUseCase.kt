package com.app.weather.domain.usecases.device

import com.app.weather.domain.models.FavoriteLocationModel
import javax.inject.Inject

class GetDefaultSavedLocationUseCase @Inject constructor(private val deviceRepository: DeviceRepository) {
    operator fun invoke(): FavoriteLocationModel? {
        return deviceRepository.getDefaultSavedLocation()?.toFavoriteLocationModel()
    }
}