package com.app.weather.data.source.device

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import com.app.weather.data.source.device.model.SavedLocation
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceDataSource @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        const val PREFERENCE_KEY = "devids13ce-p24refer1212ence-sdlk23p"
        const val SAVED_LOCATION_KEY = "s3oaved-12locu4atio3n-lj2k430asd"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    fun updateSavedLocations(locations: List<SavedLocation>) {
        getSavedLocations().toMutableList().apply {
            addAll(locations)
            val converter = Gson()
            val stringSet = this.map {
                converter.toJson(it)
            }.toSet()
            sharedPreferences.edit().putStringSet(
                SAVED_LOCATION_KEY, stringSet
            ).apply()
        }

    }

    fun getSavedLocations(): List<SavedLocation> {
        val stringSet = sharedPreferences.getStringSet(SAVED_LOCATION_KEY, emptySet())
        val gsonConverter = Gson()
        if (stringSet.isNullOrEmpty()) return emptyList()
        return stringSet.filterNotNull().map {
            gsonConverter.fromJson(it, SavedLocation::class.java)
        }.sortedBy { it.name }
    }

    fun deleteSavedLocations(locations: List<SavedLocation>) {
        val updatedList = getSavedLocations().toMutableList().also {
            it.removeIf { locationModel ->
                locations.contains(locationModel)
            }
        }
        clearSavedLocations()
        updateSavedLocations(updatedList)
    }

    fun clearSavedLocations() {
        sharedPreferences.edit().putStringSet(SAVED_LOCATION_KEY, emptySet()).apply()
    }

    fun getDefaultSavedLocation(): SavedLocation? {
        return getSavedLocations().firstOrNull { locationModel ->
            locationModel.isDefault
        }
    }


    fun getDeviceRegion(): String {
        return TimeZone.getDefault().id
    }

    fun getSystemCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }
}