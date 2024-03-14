package com.app.weather.presentation.screen.weather_detail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.weather.domain.models.FavoriteLocationModel
import com.app.weather.domain.usecases.device.DeleteSavedLocationUseCase
import com.app.weather.domain.usecases.device.GetDefaultSavedLocationUseCase
import com.app.weather.domain.usecases.device.GetSavedLocationsUseCase
import com.app.weather.domain.usecases.device.GetSystemCurrentTimeInMillisUseCase
import com.app.weather.domain.usecases.device.UpdateSavedLocationsUseCase
import com.app.weather.domain.usecases.weather.FetchHourlyForecastDataUseCase
import com.app.weather.domain.usecases.weather.SearchCityUseCase
import com.app.weather.infrastructure.util.toFavoriteLocationModel
import com.app.weather.presentation.screen.WeatherNavigationRoute
import com.app.weather.presentation.screen.home.HomeViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    fetchHourlyForecastDataUseCase: FetchHourlyForecastDataUseCase,
    getSystemCurrentTimeInMillisUseCase: GetSystemCurrentTimeInMillisUseCase,
    getSavedLocationsUseCase: GetSavedLocationsUseCase,
    getDefaultSavedLocationUseCase: GetDefaultSavedLocationUseCase,
    updateSavedLocationsUseCase: UpdateSavedLocationsUseCase,
    val deleteSavedLocationUseCase: DeleteSavedLocationUseCase,
    searchCityUseCase: SearchCityUseCase

) : HomeViewModel(
    savedStateHandle,
    fetchHourlyForecastDataUseCase,
    getSystemCurrentTimeInMillisUseCase,
    getDefaultSavedLocationUseCase,
    getSavedLocationsUseCase,
    searchCityUseCase,
    updateSavedLocationsUseCase
) {

    private lateinit var favoriteLocationModel: FavoriteLocationModel

    init {
        fetchWeatherDetail()
    }

    private fun fetchWeatherDetail() {

        val locationString: String =
            savedStateHandle[WeatherNavigationRoute.SearchResult.argumentName] ?: ""

        favoriteLocationModel = locationString.toFavoriteLocationModel()
        viewModelScope.launch {
            val response =
                fetchHourlyForecastDataUseCase.invoke(favoriteLocationModel.getDisplayName(), 5)
            if (response is com.app.weather.domain.Result.Success) {
                filterForecastedWeatherResult(response.data)
            }
        }
    }

    fun onSaveLocationTapped() {
        val currentlySavedLocations = getSavedLocationsUseCase.invoke()
        currentlySavedLocations.firstOrNull() { locationModel ->
            locationModel.getIdentifier() == favoriteLocationModel.getIdentifier()
        }.apply {
            if (this == null) {
                updateSavedLocationsUseCase.invoke(listOf(favoriteLocationModel))
            } else {
                deleteSavedLocationUseCase.invoke(listOf(favoriteLocationModel))
            }

        }
    }
}