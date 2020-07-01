package com.demo.testweatherapp.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.demo.testweatherapp.location.LocationLiveData


class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData =
        LocationLiveData(application)
    fun getLocationData() = locationData

}