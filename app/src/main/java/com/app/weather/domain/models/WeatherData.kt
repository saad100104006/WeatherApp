package com.app.weather.domain.models

sealed interface WeatherData {
    val location: String
    val region: String
    val country: String
    val localTime: String
    val localTimeEpoch: Long

    fun getIdentifier(): String {
        return "$location $region $country"
    }

    data class Hourly(
        override val location: String,
        override val region: String,
        override val country: String,
        override val localTime: String,
        override val localTimeEpoch: Long,

        //hourly weather data
        val timeEpoch: Long,
        val time: String,
        val temp: Float,
        val isDay: Boolean,
        val conditionDescription: String,
        val windSpeed: Float,
        val windDegree: Float,
        val windDirection: String,
        val precipitation: Float,
        val humidity: Int,
        val cloud: Int,
        val rainingChance: Int,
        val snowingChance: Int


    ) : WeatherData

    data class Daily(
        override val location: String,
        override val region: String,
        override val country: String,
        override val localTime: String,
        override val localTimeEpoch: Long,
        //daily weather data
        val date: String,
        val updatedAtEpoch: Long,
        val updatedAtTimeString: String,
        val tempAverage: Float,
        val tempMinimum: Float?,
        val tempMaximum: Float?,
        val isDay: Boolean,
        val conditionDescription: String,
        val windSpeed: Float,
        val windDegree: Float,
        val precipitation: Float,
        val humidity: Float,
        val cloud: Int,

        //hourly
        val hourlyData: List<Hourly>?

    ) : WeatherData
}