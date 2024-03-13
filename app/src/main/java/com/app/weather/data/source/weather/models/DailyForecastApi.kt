package com.app.weather.data.source.weather.models

import com.google.gson.annotations.SerializedName

data class DailyForecastApiModel(
    @SerializedName("forecastday")
    val dailyForecastData: DailyForecastDataList
)

typealias DailyForecastDataList = List<DailyForecast>

data class DailyForecast(
    val date: String,
    @SerializedName("date_epoch")
    val dateEpoch: String,
    @SerializedName("day")
    val dailySummary: DailySummaryData,
    val astro: AstroData,
    @SerializedName("hour")
    val hourlyForecastDataList: HourlyForecastDataList
)

typealias HourlyForecastDataList = List<HourlyForecastData>


data class DailySummaryData(
    @SerializedName("maxtemp_c")
    val maxTemperatureCentigrade: Float,
    @SerializedName("mintemp_c")
    val minTemperatureCentigrade: Float,
    @SerializedName("avgtemp_c")
    val avgTemperatureCentigrade: Float,
    @SerializedName("maxwind_kph")
    val maxWindKph: Float,
    @SerializedName("totalprecip_mm")
    val totalPrecipitationMillimetre: Float,
    @SerializedName("totalsow_cm")
    val totalSnowCentimetre: Float,
    @SerializedName("avghumidity")
    val avgHumidity: Float,
    @SerializedName("daily_chance_of_rain")
    val rainingChance: Int,
    @SerializedName("daily_chance_of_sonw")
    val snowingChance: Int,
    val condition: WeatherConditionApiModel,
)

data class AstroData(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerializedName("moon_phase")
    val moonPhase: String,
    @SerializedName("moon_illumination")
    val moonIllumination:Int,
    @SerializedName("is_moon_up")
    val isMoonUp: Int,
    @SerializedName("is_sun_up")
    val isSunUp: Int
)

data class HourlyForecastData(
    @SerializedName("time_epoch")
    val timeEpoch: Long,
    val time: String,
    @SerializedName("temp_c")
    val temperatureCentigrade: Float,
    @SerializedName("is_day")
    val isDay: Int,
    val condition: WeatherConditionApiModel,
    @SerializedName("wind_kph")
    val windSpeed: Float,
    @SerializedName("wind_degree")
    val windDegree: Float,
    @SerializedName("wind_dir")
    val windDirection: String,
    @SerializedName("precip_mm")
    val precipitationMillimetre: Float,
    val humidity: Int,
    val cloud: Int,
    @SerializedName("feelslike_c")
    val feelsLikeTempCentigrade: Float,
    @SerializedName("chance_of_rain")
    val rainingChance: Int,
    @SerializedName("chance_of_snow")
    val snowingChance: Int,
)