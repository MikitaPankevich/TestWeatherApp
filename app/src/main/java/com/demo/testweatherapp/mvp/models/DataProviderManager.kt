package com.demo.testweatherapp.mvp.models

import androidx.lifecycle.LiveData
import com.demo.testweatherapp.pojo.Base

object DataProviderManager {

    var base: Base? = null

    fun registerDataProvider(base: Base) {
        DataProviderManager.base = base
    }

}