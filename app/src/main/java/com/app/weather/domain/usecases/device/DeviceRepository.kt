package com.app.weather.domain.usecases.device

import com.app.weather.data.source.device.model.SavedLocation

interface DeviceRepository {

    fun getSavedLocations(): List<SavedLocation>

    fun updateSavedLocations(locations: List<SavedLocation>)

    fun deleteSavedLocation(locations: List<SavedLocation>)

    fun clearSavedLocations()

    fun getDefaultSavedLocation(): SavedLocation?


    fun getDeviceRegion():String

    fun getSystemCurrentTimeInMillis(): Long
}