package com.app.weather.presentation.framework.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.weather.R

@Composable
fun SimpleWind() {
    SimpleDrawableIcon(
        iconResource = R.drawable.ic_wind,
        contentDescription = R.string.content_description_wind
    )
}

@Composable
fun SimpleWindStat(wind: Float) {
    SimpleWeatherStat(
        title = stringResource(id = R.string.weather_stat_wind),
        icon = { SimpleWind() },
        stat = String.format(stringResource(id = R.string.quantity_kilometer_per_hour), wind)
    )
}

@Composable
@Preview
fun SimpleWindStatPreview() {
    SimpleWindStat(wind = 24f)
}

@Composable
fun SimpleHumidity() {
    SimpleDrawableIcon(iconResource = R.drawable.ic_humidity)
}

@Composable
fun SimpleHumidityStat(humidity: Float) {
    SimpleWeatherStat(
        title = stringResource(id = R.string.weather_stat_humidity),
        icon = { SimpleHumidity() },
        stat = String.format(
            stringResource(id = R.string.quantity_percent), humidity
        )
    )
}

@Composable
@Preview
private fun SimpleHumidityStatPreview() {
    SimpleHumidityStat(humidity = 25f)
}

@Composable
fun SimpleRain() {
    SimpleDrawableIcon(iconResource = R.drawable.ic_rain)
}

@Composable
fun SimpleRainStat(rain: Float) {
    SimpleWeatherStat(
        title = stringResource(id = R.string.weather_stat_rain),
        icon = { SimpleRain() },
        stat = String.format(
            stringResource(id = R.string.quantity_percent), rain
        )
    )
}

@Composable
@Preview
private fun SimpleRainStatPreview() {
    SimpleRainStat(rain = 25f)
}

@Composable
fun SimpleTemperature() {
    SimpleDrawableIcon(iconResource = R.drawable.ic_temperature)
}

@Composable
fun SimpleTemperatureStat(temperature: Float) {
    SimpleWeatherStat(
        title = stringResource(id = R.string.weather_stat_temperature),
        icon = { SimpleTemperature() },
        stat = String.format(
            stringResource(id = R.string.quantity_percent), temperature
        )
    )
}

@Composable
@Preview
private fun SimpleTemperatureStatPreview() {
    SimpleTemperatureStat(temperature = 44f)
}

@Composable
private fun SimpleDrawableIcon(
    iconResource: Int,
    contentDescription: Int = R.string.content_description_none
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = iconResource),
        contentDescription = stringResource(id = contentDescription),
        tint = Color.Gray
    )
}

@Composable
fun SimpleWeatherCondition(iconResource: Int, description: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconResource),
            contentDescription = stringResource(id = R.string.content_description_none),
            tint = Color.Gray
        )
        Text(text = description)
    }
}

@Composable
fun SimpleHorizontalLine() {
    Box(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
                .height(1.dp)
        )
    }


}

@Composable
fun SimpleWeatherStat(
    title: String? = null,
    icon: @Composable () -> Unit,
    stat: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        icon.invoke()
        Text(text = stat, fontWeight = FontWeight.Light, fontFamily = FontFamily.Monospace)
        title?.let {
            Text(text = title, color = Color.Gray)
        }
    }
}