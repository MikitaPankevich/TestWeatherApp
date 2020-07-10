package com.demo.testweatherapp.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.demo.testweatherapp.R
import com.demo.testweatherapp.data.DataProviderManager
import com.demo.testweatherapp.pojo.Info
import kotlin.math.roundToInt

class HoursViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val txtHours: TextView = itemView.findViewById(R.id.textViewHours)
    private val imgWeatherPic: ImageView = itemView.findViewById(R.id.imageViewForecastWeatherPicture)
    private val txtCurrentWeather: TextView = itemView.findViewById(R.id.textViewCurrentWeather)
    private val txtTemperature: TextView = itemView.findViewById(R.id.textViewTemperature)
    private val viewLine: View = itemView.findViewById(R.id.viewBottomLine)

    internal fun setHoursDetails(info: Info) {
        txtHours.text = DataProviderManager.getTime(info.dt_txt)
        txtTemperature.text = String.format("%s%s",(info.main.temp - 273.15).roundToInt().toString(), itemView.context.getString(R.string.temperatureMark))
        txtCurrentWeather.text = info.weather[0].description.capitalize()
        DataProviderManager.chooseImage(imgWeatherPic, info.weather[0].main, DataProviderManager.getTime(info.dt_txt))
        viewLine.isVisible = DataProviderManager.getTime(info.dt_txt) != itemView.context.getString(R.string.timeBeforeDayOfWeek)
    }
}