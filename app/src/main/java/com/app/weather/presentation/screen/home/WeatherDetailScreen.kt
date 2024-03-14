package com.app.weather.presentation.screen.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.weather.R
import com.app.weather.domain.models.WeatherData
import com.app.weather.infrastructure.util.DateUtil
import com.app.weather.presentation.framework.components.CenterContentTopAppBar
import com.app.weather.presentation.framework.components.LargeVerticalSpacer
import com.app.weather.presentation.framework.components.MediumVerticalSpacer
import com.app.weather.presentation.framework.components.SimpleHorizontalLine
import com.app.weather.presentation.framework.components.SimpleHumidity
import com.app.weather.presentation.framework.components.SimpleHumidityStat
import com.app.weather.presentation.framework.components.SimpleRain
import com.app.weather.presentation.framework.components.SimpleRainStat
import com.app.weather.presentation.framework.components.SimpleTemperature
import com.app.weather.presentation.framework.components.SimpleWeatherCondition
import com.app.weather.presentation.framework.components.SimpleWeatherStat
import com.app.weather.presentation.framework.components.SimpleWindStat
import com.app.weather.presentation.framework.components.SmallVerticalSpacer
import com.app.weather.presentation.framework.components.TinyHorizontalSpacer
import com.app.weather.presentation.framework.components.TinyVerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailScreen(
    weatherDataState: State<List<WeatherData.Daily>?>,
    paddingValues: PaddingValues,
    onAppBarStartIconTapped: () -> Unit,
    onAppBarEndIconTapped: () -> Unit,
    appbarStartIcon: ImageVector? = null,
    appbarEndIcon: ImageVector? = null
) {
    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) {
        Screen(
            weatherDataState = weatherDataState,
            paddingValues = it,
            onAppBarEndIconTapped = onAppBarEndIconTapped,
            onAppbarStartIconTapped = onAppBarStartIconTapped,
            appbarStartIcon = appbarStartIcon,
            appbarEndIcon = appbarEndIcon

        )
    }
}

@Composable
fun Screen(
    weatherDataState: State<List<WeatherData.Daily>?>,
    paddingValues: PaddingValues,
    onAppBarEndIconTapped: () -> Unit,
    onAppbarStartIconTapped: () -> Unit,
    appbarStartIcon: ImageVector?,
    appbarEndIcon: ImageVector?
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        CenterContentTopAppBar(
            title = { Text(weatherDataState.value?.firstOrNull()?.location ?: "", color = Color.Gray) },
            startIcon = appbarStartIcon,
            onStartIconClicked = onAppbarStartIconTapped,
            endIcon = appbarEndIcon,
            onEndIconClicked = onAppBarEndIconTapped
        )
        Content(
            weatherDataState = weatherDataState,
        )

    }
}

@Composable
fun Content(
    weatherDataState: State<List<WeatherData.Daily>?>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmallVerticalSpacer()
        //Date
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_calendar,),
                contentDescription = stringResource(
                    id = R.string.content_description_date
                ),
                tint = Color.Gray
            )
            TinyHorizontalSpacer()
            Text(
                DateUtil.getFormattedDateMonth(
                    weatherDataState.value?.firstOrNull()?.localTime ?: ""
                ),
                color = Color.Gray
            )
        }
        SmallVerticalSpacer()

        //Temperature
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 38.dp)
        ) {
            Text(
                ((weatherDataState.value?.firstOrNull()?.tempAverage ?: "0").toString()),
                fontSize = 130.sp
            )
            Text(stringResource(id = R.string.degree_centigrade_placeholder), fontSize = 38.sp)
        }

        SmallVerticalSpacer()
        //weather condition
        SimpleWeatherCondition(
            iconResource = R.drawable.ic_cloudy,
            description = weatherDataState.value?.firstOrNull()?.conditionDescription ?: ""
        )

        MediumVerticalSpacer()

        //Weather stat
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                SimpleWindStat(wind = weatherDataState.value?.firstOrNull()?.windSpeed ?: 0f)

            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                SimpleHumidityStat(humidity = weatherDataState.value?.firstOrNull()?.humidity ?: 0f)

            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                SimpleRainStat(rain = weatherDataState.value?.firstOrNull()?.precipitation ?: 0f)

            }
        }

        MediumVerticalSpacer()
        HourlyWeatherStat(
            items = weatherDataState.value?.firstOrNull()?.hourlyData ?: mutableListOf()
        )
        LargeVerticalSpacer()
        DailyWeatherForecast(weatherDataState.value ?: emptyList())
        MediumVerticalSpacer()
    }

}


@Composable
fun HourlyWeatherStat(items: List<WeatherData.Hourly>) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        items.forEach {
            TinyHorizontalSpacer()
            SimpleWeatherStat(
                title = DateUtil.getFormattedDateTime(it.time),
                icon = { SimpleRain() },
                stat = "${it.temp}°"
            )
            TinyHorizontalSpacer()
        }
    }
}

@Composable
fun DailyWeatherForecast(items: List<WeatherData.Daily?>) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_calendar),
                    contentDescription = stringResource(
                        id = R.string.content_description_none
                    ),
                    tint = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SimpleTemperature()
                SimpleHumidity()
                SimpleRain()
            }

        }
        SimpleHorizontalLine()
        Column() {
            items.forEach {
                it?.let {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(DateUtil.getFormattedDateMonthShort(it.date))

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                String.format(
                                    stringResource(id = R.string.quantity_centigrade),
                                    it.tempAverage
                                )
                            )
                            Text(
                                String.format(
                                    stringResource(id = R.string.quantity_percent),
                                    it.humidity
                                )
                            )
                            Text(
                                String.format(
                                    stringResource(id = R.string.quantity_percent),
                                    it.precipitation
                                )
                            )
                        }

                    }
                }
                TinyVerticalSpacer()

            }

        }
    }
}
