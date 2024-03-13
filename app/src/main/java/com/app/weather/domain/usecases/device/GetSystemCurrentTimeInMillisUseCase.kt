package com.app.weather.domain.usecases.device

import com.app.weather.data.source.device.DeviceDataSource
import javax.inject.Inject

class GetSystemCurrentTimeInMillisUseCase @Inject constructor(private val deviceDataSource: DeviceDataSource) {
    operator fun invoke(): Long {
        return deviceDataSource.getSystemCurrentTimeInMillis()
    }
}