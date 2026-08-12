package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connect MainActivity with activity_main.xml
        setContentView(R.layout.activity_main)
    }
}