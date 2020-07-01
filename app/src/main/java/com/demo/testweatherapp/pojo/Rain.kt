package com.demo.testweatherapp.pojo

import com.google.gson.annotations.SerializedName

data class Rain(

    @SerializedName("3h")
    val humidity: Double
)