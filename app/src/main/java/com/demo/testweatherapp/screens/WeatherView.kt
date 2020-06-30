package com.demo.testweatherapp.screens

import android.content.Context
import android.util.Log
import com.demo.testweatherapp.pojo.Base
import com.demo.testweatherapp.pojo.Info

interface WeatherView {
    fun showData()
    fun showError(error: String?){
        Log.d("DEBUG_LoadData",error)
    }
}