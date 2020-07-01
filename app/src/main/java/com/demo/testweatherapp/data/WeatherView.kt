package com.demo.testweatherapp.data

import android.util.Log

interface WeatherView {
    fun showData()
    fun showError(error: String?) {
        Log.d("DEBUG_LoadData", error)
    }
}