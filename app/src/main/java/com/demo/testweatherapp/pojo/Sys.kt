package com.demo.testweatherapp.pojo

import com.google.gson.annotations.SerializedName

data class Sys (

    @SerializedName("pod")
    val pod : String
)