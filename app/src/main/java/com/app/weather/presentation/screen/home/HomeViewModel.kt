package com.app.weather.presentation.screen.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.weather.domain.Result
import com.app.weather.domain.models.FavoriteLocationModel
import com.app.weather.domain.models.WeatherData
import com.app.weather.domain.usecases.device.GetDefaultSavedLocationUseCase
import com.app.weather.domain.usecases.device.GetSavedLocationsUseCase
import com.app.weather.domain.usecases.device.GetSystemCurrentTimeInMillisUseCase
import com.app.weather.domain.usecases.device.UpdateSavedLocationsUseCase
import com.app.weather.domain.usecases.weather.FetchHourlyForecastDataUseCase
import com.app.weather.domain.usecases.weather.SearchCityUseCase
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
open class HomeViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val fetchHourlyForecastDataUseCase: FetchHourlyForecastDataUseCase,
    private val getSystemCurrentTimeInMillisUseCase: GetSystemCurrentTimeInMillisUseCase,
    private val getDefaultSavedLocationUseCase: GetDefaultSavedLocationUseCase,
    val getSavedLocationsUseCase: GetSavedLocationsUseCase,
    private val searchCityUseCase: SearchCityUseCase,
    val updateSavedLocationsUseCase: UpdateSavedLocationsUseCase
) : ViewModel() {
    val forecastedWeatherData: MutableLiveData<List<WeatherData.Daily>> = MutableLiveData()
    val isLocationUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshedCalledLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _isGetLocationUpdate: MutableLiveData<Boolean> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        if (getDefaultSavedLocationUseCase.invoke() != null) {
            fetchDefaultLocationWeatherData()
        }
    }


    private fun fetchDefaultLocationWeatherData(isRefreshedCalled:Boolean = false) {
        val location = getDefaultSavedLocationUseCase.invoke()
        val searchString = "${location?.name} ${location?.country}"
        viewModelScope.launch {
            val response =
                fetchHourlyForecastDataUseCase.invoke(searchString, 5)
            if (response is Result.Success) {
                filterForecastedWeatherResult(response.data, isRefreshedCalled)
            } else if (response is Result.Error) {
                errorLiveData.value = response.exception.message
            }
        }
    }


     fun filterForecastedWeatherResult(result: List<WeatherData.Daily>, isRefreshedCalled:Boolean = false) {
        viewModelScope.launch {
            forecastedWeatherData.value = emptyList()
            val filteredHourlyData = result[0].hourlyData?.toMutableList()
            filteredHourlyData?.removeIf {
                TimeUnit.SECONDS.toMillis(it.timeEpoch) < getSystemCurrentTimeInMillisUseCase.invoke()
            }
            val newList = mutableListOf<WeatherData.Daily>()
            result.forEachIndexed { index, item ->
                newList.add(
                    WeatherData.Daily(
                        location = item.location,
                        country = item.country,
                        localTime = item.localTime,
                        localTimeEpoch = item.localTimeEpoch,
                        updatedAtEpoch = item.updatedAtEpoch,
                        updatedAtTimeString = item.updatedAtTimeString,
                        tempAverage = item.tempAverage,
                        tempMinimum = item.tempMinimum,
                        tempMaximum = item.tempMaximum,
                        isDay = item.isDay,
                        conditionDescription = item.conditionDescription,
                        windSpeed = item.windSpeed,
                        windDegree = item.windDegree,
                        precipitation = item.precipitation,
                        humidity = item.humidity,
                        cloud = item.cloud,
                        hourlyData = if (index == 0) filteredHourlyData else item.hourlyData,
                        region = item.region,
                        date = item.date
                    )
                )
            }
            forecastedWeatherData.value = newList
            if(newList.isNotEmpty()) {
                isLocationUpdated.value = true
            } else {
                errorLiveData.value = "Location update is not working"
            }
            if(isRefreshedCalled){
                isRefreshedCalledLiveData.value = true
            }
        }

    }

     fun refreshFavoriteLocationData(deviceLocation: String?, isRefreshedCalled:Boolean =false, isGetLocationUpdate: Boolean  =false) {
         val cityAddress = deviceLocation
         if(isGetLocationUpdate && _isGetLocationUpdate.value == true){
             //we donnot need to make API call on each location update, we have refresh button to get updates
             return
         }
        viewModelScope.launch {
            val result = searchCityUseCase.invoke(cityAddress)
            when (result) {
                is Result.Success -> {
                    val searchResultModel = result.data.firstOrNull()
                    if (searchResultModel == null) {
                        errorLiveData.value = "Location data could not be determined"
                        Log.e("XWeather", "Location data could not be determined")
                    } else {
                        updateSavedLocationsUseCase.invoke(
                            listOf(
                                FavoriteLocationModel(
                                    id = searchResultModel.id,
                                    name = searchResultModel.name,
                                    region = searchResultModel.region,
                                    country = searchResultModel.country,
                                    isDefault = true
                                )
                            )
                        )
                        Log.e("XWeather", "Location data successfully saved")
                        fetchDefaultLocationWeatherData(isRefreshedCalled)
                    }
                }

                is Result.Error -> {
                    errorLiveData.value = "Unknown network error"
                    Log.e("XWeather", "Unknown network error")
                }
            }
        }
         if(isGetLocationUpdate && cityAddress!=null){
             _isGetLocationUpdate.value = true
         }

    }

}

