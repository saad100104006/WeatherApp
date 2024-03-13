package com.app.weather.domain.usecases.device

import javax.inject.Inject

class ClearSavedLocationsUseCase @Inject constructor(private val deviceRepository: DeviceRepository) {
    operator fun invoke(){
        deviceRepository.clearSavedLocations()
    }
}