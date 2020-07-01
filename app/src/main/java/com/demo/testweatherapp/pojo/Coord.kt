package com.demo.testweatherapp.pojo

import com.google.gson.annotations.SerializedName

data class Coord(

    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)