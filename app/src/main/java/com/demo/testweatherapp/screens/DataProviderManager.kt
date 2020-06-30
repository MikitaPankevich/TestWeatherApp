package com.demo.testweatherapp.screens

import android.util.Log
import com.demo.testweatherapp.pojo.Base

object DataProviderManager {

    var base: Base? = null

    fun registerDataProvider(base: Base) {
        this.base = base
    }

}