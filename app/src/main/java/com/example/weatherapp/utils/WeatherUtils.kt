package com.example.weatherapp.utils

object WeatherUtils {

    fun getWeatherCondition(code: Int?): String {
        return when (code) {

            0 -> "Clear Sky"

            1 -> "Mainly Clear"
            2 -> "Partly Cloudy"
            3 -> "Overcast"

            45, 48 -> "Fog"

            51, 53, 55 -> "Drizzle"
            56, 57 -> "Freezing Drizzle"

            61, 63, 65 -> "Rain"
            66, 67 -> "Freezing Rain"

            71, 73, 75 -> "Snowfall"
            77 -> "Snow Grains"

            80, 81, 82 -> "Rain Showers"
            85, 86 -> "Snow Showers"

            95 -> "Thunderstorm"
            96, 99 -> "Thunderstorm with Hail"

            else -> "Unknown"
        }
    }
}