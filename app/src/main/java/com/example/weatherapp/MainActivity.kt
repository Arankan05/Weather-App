package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.network.RetrofitClient
import com.example.weatherapp.utils.WeatherUtils
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etCity: EditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvMessage: TextView
    private lateinit var weatherCard: CardView

    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCondition: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        etCity = findViewById(R.id.etCity)
        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        tvMessage = findViewById(R.id.tvMessage)
        weatherCard = findViewById(R.id.weatherCard)

        tvCity = findViewById(R.id.tvCity)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvCondition = findViewById(R.id.tvCondition)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)

        // Hide card initially
        weatherCard.visibility = View.GONE
    }

    private fun setupListeners() {
        btnSearch.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isEmpty()) {
                showError("Please enter a city name")
                return@setOnClickListener
            }
            fetchWeather(city)
        }
    }

    private fun fetchWeather(city: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)

                // 1. Geocoding
                val geoResponse = RetrofitClient.geocodingApi.searchCity(city)
                val location = geoResponse.results?.firstOrNull()

                if (location == null || location.latitude == null || location.longitude == null) {
                    showError("City not found. Please try another city.")
                    return@launch
                }

                // 2. Weather
                val weatherResponse = RetrofitClient.weatherApi.getCurrentWeather(
                    location.latitude,
                    location.longitude
                )

                val current = weatherResponse.current
                if (current == null) {
                    showError("Unable to fetch weather. Please try again.")
                    return@launch
                }

                // 3. Update UI
                updateUI(
                    cityName = location.name ?: city,
                    temp = current.temperature_2m,
                    conditionCode = current.weather_code,
                    humidity = current.relative_humidity_2m,
                    windSpeed = current.wind_speed_10m
                )

            } catch (e: Exception) {
                e.printStackTrace()
                showError("Unable to fetch weather. Check your internet connection.")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun updateUI(
        cityName: String,
        temp: Double?,
        conditionCode: Int?,
        humidity: Int?,
        windSpeed: Double?
    ) {
        tvCity.text = cityName
        tvTemperature.text = String.format(Locale.getDefault(), "%.1f°C", temp ?: 0.0)
        tvCondition.text = WeatherUtils.getWeatherCondition(conditionCode)
        tvHumidity.text = "${humidity ?: 0}%"
        tvWindSpeed.text = "${windSpeed ?: 0.0} km/h"

        weatherCard.visibility = View.VISIBLE
        tvMessage.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnSearch.isEnabled = !isLoading
        if (isLoading) {
            weatherCard.visibility = View.GONE
            tvMessage.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        tvMessage.text = message
        tvMessage.visibility = View.VISIBLE
        weatherCard.visibility = View.GONE
        progressBar.visibility = View.GONE
    }
}