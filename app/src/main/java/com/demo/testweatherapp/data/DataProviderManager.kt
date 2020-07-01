package com.demo.testweatherapp.data

import android.widget.ImageView
import com.demo.testweatherapp.R
import com.demo.testweatherapp.pojo.Base
import com.squareup.picasso.Picasso

object DataProviderManager {

    var base: Base? = null

    fun registerDataProvider(base: Base) {
        DataProviderManager.base = base
    }

    fun chooseImage(imageView: ImageView, weather: String?, time: String) {
        var time: Int = time.dropLast(3).toInt()
        when (weather) {
            "Clear" -> if (time in 6..20) {
                Picasso.get().load(R.drawable.weather_sunny_yellow).into(imageView)
            } else {
                Picasso.get().load(R.drawable.weather_night).into(imageView)
            }
            "Clouds" -> if (time in 6..20) {
                Picasso.get().load(R.drawable.weather_cloudy).into(imageView)
            } else {
                Picasso.get().load(R.drawable.weather_night_partly_cloudy).into(imageView)
            }
            "Rain" -> Picasso.get().load(R.drawable.weather_rainy).into(imageView)
            "Drizzle" -> Picasso.get().load(R.drawable.weather_rainy).into(imageView)
            "Thunderstorm" -> Picasso.get().load(R.drawable.weather_lightning).into(imageView)
            "Snow" -> Picasso.get().load(R.drawable.weather_snowy_heavy).into(imageView)
            "Mist" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Smoke" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Haze" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Dust" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Fog" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Sand" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Ash" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Squall" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Tornado" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
        }
    }


    fun getTime(date: String): String {
        //return from "2017-01-31 03:00:00" to "03:00"
        return date.drop(11).dropLast(3)
    }

}