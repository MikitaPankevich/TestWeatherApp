package com.demo.testweatherapp.mvp.views

import com.arellomobile.mvp.MvpView
import com.demo.testweatherapp.pojo.Base

interface ForecastView: MvpView {
    fun showData(base: Base)
    fun showAlert(text: MutableList<String>?)
    fun hideScreen()
    fun showScreen()
}