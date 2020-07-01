package com.demo.testweatherapp.pojo

import com.google.gson.annotations.SerializedName

data class Base(

    @SerializedName("cod")
    val cod: Int,
    @SerializedName("message")
    val message: Int,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("list")
    val list: List<Info>,
    @SerializedName("city")
    val city: City
)