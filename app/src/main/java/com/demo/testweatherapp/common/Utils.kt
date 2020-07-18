package com.demo.testweatherapp.common

import android.annotation.SuppressLint
import android.widget.ImageView
import com.demo.testweatherapp.R
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import com.demo.testweatherapp.common.Months.*

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

     @SuppressLint("SimpleDateFormat")
     fun getDayOfWeek(data: String): String {
        // from 2017-03-31 to Monday
        val date: Date = SimpleDateFormat("yyyy-MM-dd").parse(data)
        val dayFormat: DateFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        return dayFormat.format(date)
    }

    fun getTime(date: String): String {
        //return from "2017-01-31 03:00:00" to "03:00"
        return date.drop(11).dropLast(3)
    }

    fun getDate(date: String): String{
        //return from "2017-01-31 03:00:00" to "2017-01-31"
        return date.dropLast(9)
    }

    fun getDayByDate(date: String): String{
        //return from "2017-01-31" to "31"
        return date.drop(8)
    }

    fun getMonthByDate(date: String): String {
          // return from"2017-01-31" to "January"
        return when (date.drop(5).dropLast(3).toInt()) {
               January.value -> "Jan"
               February.value -> "Feb"
               March.value -> " Mar"
               April.value -> "Apr"
               May.value -> "May"
               June.value -> "June"
               July.value -> "July"
               August.value -> "Aug"
               September.value -> "Sept"
               October.value -> "Oct"
               November.value -> "Nov"
               December.value -> "Dec"
               else -> "Unknown month"
        }
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