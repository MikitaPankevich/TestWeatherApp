package com.demo.testweatherapp.api

import com.demo.testweatherapp.pojo.Base
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("2.5/forecast")
    fun getForecast(
        @Query(API_KEY) apiKey: String = "5f81c993a5353404370a4003ececece6",
        @Query(LATITUDE) lat: Double,
        @Query(LONGITUDE) lon: Double
    ): Single<Base>

    companion object{
        private const val API_KEY = "appid"
        private const val LATITUDE = "lat"
        private const val LONGITUDE= "lon"
    }
}