package com.app.weather.domain.usecases.device

import com.app.weather.data.source.device.DeviceDataSource
import com.app.weather.domain.models.FavoriteLocationModel
import javax.inject.Inject

class DeleteSavedLocationUseCase @Inject constructor(private val deviceDataSource: DeviceDataSource) {
    operator fun invoke(locations: List<FavoriteLocationModel>) {
        deviceDataSource.deleteSavedLocations(locations = locations.map { it.toSavedLocationModel()})
    }
}