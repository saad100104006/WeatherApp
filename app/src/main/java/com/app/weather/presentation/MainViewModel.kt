package com.app.weather.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.weather.domain.models.FavoriteLocationModel

class MainViewModel
: ViewModel() {
    val onNavigateToSearch: MutableLiveData<Boolean> = MutableLiveData(false)
    val onNavigateBack: MutableLiveData<Boolean> = MutableLiveData(false)
    val onShowWeatherDetail: MutableLiveData<FavoriteLocationModel> = MutableLiveData()

    fun onNavigateToSearch() {
        onNavigateToSearch.value = true
    }

    fun onNavigateToSearchResolved() {
        onNavigateToSearch.value = false
    }

    fun onNavigateBack() {
        onNavigateBack.value = true
    }

    fun onNavigateBackResolved() {
        onNavigateBack.value = false
    }

    fun onShowWeatherDetail(locationModel: FavoriteLocationModel) {
        onShowWeatherDetail.value = locationModel
    }

    fun onShowWeatherDetailResolved() {
        onShowWeatherDetail.value = null
    }

}