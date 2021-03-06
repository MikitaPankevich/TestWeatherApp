package com.demo.testweatherapp.mvp.views

import com.arellomobile.mvp.MvpView
import com.demo.testweatherapp.pojo.Base

interface TodayView: MvpView {
    fun showData(base: Base)
    fun hideScreen()
    fun showScreen()
    fun createShareIntent(base: Base)
}