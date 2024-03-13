package com.app.weather.presentation.screen

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class WeatherNavigationRoute {
    object SearchResult : WeatherNavigationRoute() {
        const val argumentName = "location"
        val args = listOf(navArgument(argumentName) {
            with(this) {
                type = NavType.StringType
                defaultValue = ""
            }
        })

        fun getName(): String {
            return "search/{$argumentName}"
        }

        fun getNavigationRequest(arg: String): String {
            return "search/$arg"
        }

    }

    object Home : WeatherNavigationRoute() {
        fun getName(): String {
            return "home"
        }
    }


    object Search : WeatherNavigationRoute() {
        fun getName(): String {
            return "search"
        }
    }

    object Favorite : WeatherNavigationRoute() {
        fun getName(): String {
            return "favorite"
        }
    }
}