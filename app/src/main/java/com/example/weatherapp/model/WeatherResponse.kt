package com.example.weatherapp.model

data class WeatherResponse(
    val latitude: Double?,
    val longitude: Double?,
    val timezone: String?,
    val current: CurrentWeather?
)

data class CurrentWeather(
    val time: String?,
    val interval: Int?,
    val temperature_2m: Double?,
    val relative_humidity_2m: Int?,
    val weather_code: Int?,
    val wind_speed_10m: Double?
)