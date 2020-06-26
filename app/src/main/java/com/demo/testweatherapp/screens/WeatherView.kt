package com.demo.testweatherapp.screens

import com.demo.testweatherapp.pojo.Base

interface WeatherView {
    fun showData(base: Base?)
    fun showError()
}