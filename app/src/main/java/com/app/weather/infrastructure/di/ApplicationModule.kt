package com.app.weather.infrastructure.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.app.weather.WeatherApplication

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    fun providesXWeatherApplication(@ApplicationContext context: Context): WeatherApplication {
        return context as WeatherApplication
    }
}