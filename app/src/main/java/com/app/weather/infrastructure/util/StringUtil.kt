package com.app.weather.infrastructure.util
class StringUtil {
    companion object {
        fun getDisplayName(name: String, region: String, country: String, delimiter: String): String {
            return if (region.isBlank()) {
                "$name$delimiter$country"
            } else {
                "$name$delimiter$region$delimiter$country"
            }
        }
    }
}