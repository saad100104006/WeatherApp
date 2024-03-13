package com.app.weather.data.repositories

import com.app.weather.data.source.device.DeviceDataSource
import com.app.weather.data.source.device.model.SavedLocation
import com.app.weather.domain.usecases.device.DeviceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(private val deviceDataSource: DeviceDataSource) :
    DeviceRepository {
    override fun getSavedLocations(): List<SavedLocation> {
        return deviceDataSource.getSavedLocations()
    }

    override fun updateSavedLocations(locations: List<SavedLocation>) {
         deviceDataSource.updateSavedLocations(locations = locations)
    }

    override fun deleteSavedLocation(locations: List<SavedLocation>) {
        deviceDataSource.deleteSavedLocations(locations = locations)
    }

    override fun clearSavedLocations() {
        deviceDataSource.clearSavedLocations()
    }

    override fun getDefaultSavedLocation(): SavedLocation? {
       return deviceDataSource.getDefaultSavedLocation()
    }

    override fun getDeviceRegion(): String {
        return deviceDataSource.getDeviceRegion()
    }

    override fun getSystemCurrentTimeInMillis(): Long {
        return deviceDataSource.getSystemCurrentTimeInMillis()
    }
}
