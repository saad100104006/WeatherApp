package com.app.weather.presentation.screen.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.weather.domain.models.SearchResultModel
import com.app.weather.domain.usecases.weather.SearchCityUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase
) :
    ViewModel() {
    val searchString: MutableLiveData<String> = MutableLiveData("")
    val autocompleteResult: MutableLiveData<List<SearchResultModel>> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData("")
    val isClearSearchIconVisible: MutableLiveData<Boolean> = MutableLiveData(false)


    fun onSearchStringChanged(newString: String) {
        isClearSearchIconVisible.value = newString.isNotEmpty()
        searchString.value = newString
        if (newString.trim().length > 2) {
            performSearch(newString)
        } else {
            autocompleteResult.value = mutableListOf()
        }
    }

    private fun performSearch(searchString: String) {
        viewModelScope.launch {
            val response = searchCityUseCase.invoke(searchString)
            if (response is com.app.weather.domain.Result.Success) {
                autocompleteResult.value = response.data
            }
            if (response is com.app.weather.domain.Result.Error) {
                errorMessage.value = response.exception.message
            }
        }
    }

    fun onSearchRecommendationItemClicked(result: SearchResultModel) {
        onSearchFieldValueCleared()
    }

    fun onSearchFieldValueCleared() {
        searchString.value = ""
        autocompleteResult.value = mutableListOf()
    }
}