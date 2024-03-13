package com.app.weather.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.weather.data.repositories.DeviceRepositoryImpl
import com.app.weather.data.repositories.WeatherRepositoryImpl
import com.app.weather.domain.usecases.device.DeviceRepository
import com.app.weather.domain.usecases.weather.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    fun bindDeviceRepository(deviceRepository: DeviceRepositoryImpl): DeviceRepository

}