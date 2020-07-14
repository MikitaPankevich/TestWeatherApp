package com.demo.testweatherapp.common

import android.widget.ImageView
import com.demo.testweatherapp.R
import com.squareup.picasso.Picasso

object Utils {
    fun chooseImage(imageView: ImageView, weather: String?, time: String) {
        //from 03:00 to 03
        val time: Int = time.dropLast(3).toInt()
        when (weather) {
            TypeOfWeather.Clear.name -> if (time in 6..20) Picasso.get().load(R.drawable.weather_sun).into(imageView)
            else Picasso.get().load(R.drawable.weather_moon).into(imageView)

            TypeOfWeather.Clouds.name -> if (time in 6..20) Picasso.get().load(R.drawable.weather_clouds_sun).into(imageView)
            else Picasso.get().load(R.drawable.weather_cloudy_night).into(imageView)

            TypeOfWeather.Rain.name -> Picasso.get().load(R.drawable.weather_rain).into(imageView)
            TypeOfWeather.Drizzle.name -> Picasso.get().load(R.drawable.weather_rain).into(imageView)
            TypeOfWeather.Thunderstorm.name -> Picasso.get().load(R.drawable.weather_thunderstorm).into(imageView)
            TypeOfWeather.Snow.name -> Picasso.get().load(R.drawable.weather_snow).into(imageView)
            else -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
        }
    }


    fun getTime(date: String): String {
        //return from "2017-01-31 03:00:00" to "03:00"
        return date.drop(11).dropLast(3)
    }

    fun chooseDirection(degree: Int): String{
        return when (degree) {
            in 23..68 -> CardinalDirections.NE
            in 69..114 -> CardinalDirections.E
            in 115..160 -> CardinalDirections.SE
            in 161..206 -> CardinalDirections.S
            in 207..252 -> CardinalDirections.SW
            in 253..298 -> CardinalDirections.W
            in 299..344 -> CardinalDirections.NW
            else -> CardinalDirections.N
        }.toString()
    }
}