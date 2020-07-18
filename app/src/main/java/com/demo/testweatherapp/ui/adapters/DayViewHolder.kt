package com.demo.testweatherapp.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.demo.testweatherapp.R
import com.demo.testweatherapp.common.Utils
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.pojo.Info
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DayViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val txtDayOfWeek: TextView = itemView.findViewById(R.id.textViewDayOfWeek)
    private val txtHour: TextView = itemView.findViewById(R.id.textViewHoursDayItem)
    private val imgPicDayItem: ImageView = itemView.findViewById(R.id.imageViewForecastWeatherPictureDayItem)
    private val txtWeatherDescription: TextView = itemView.findViewById(R.id.textViewCurrentWeatherDayItem)
    private val txtTempDayItem: TextView = itemView.findViewById(R.id.textViewTemperatureDayItem)

    internal fun setDayDetails(info: Info) {
        txtDayOfWeek.text =  String.format("%s, %s %s",Utils.getDayOfWeek(Utils.getDate(info.dt_txt)),Utils.getMonthByDate(Utils.getDate(info.dt_txt)), Utils.getDayByDate(Utils.getDate(info.dt_txt)))
        txtHour.text = Utils.getTime(info.dt_txt)
        txtWeatherDescription.text = info.weather[0].description.capitalize()
        Utils.chooseImage(imgPicDayItem, info.weather[0].main, Utils.getTime(info.dt_txt))
        txtTempDayItem.text = String.format("%s%s",(info.main.temp - 273.15).roundToInt().toString(), itemView.context.getString(R.string.temperatureMark))
    }
}