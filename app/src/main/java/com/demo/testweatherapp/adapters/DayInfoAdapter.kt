package com.demo.testweatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.demo.testweatherapp.R
import com.demo.testweatherapp.pojo.Info
import com.demo.testweatherapp.data.DataProviderManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class DayInfoAdapter(context: Context?, info: List<Info>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context? = context

    private val TYPE_DAY = 1
    private val TYPE_HOURS = 2

    private var fiveDayInfoList: List<Info> = info
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    internal class DayViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtDayOfWeek: TextView = itemView.findViewById(R.id.textViewDayOfWeek)
        private val txtHour: TextView = itemView.findViewById(R.id.textViewHoursDayItem)
        private val imgPicDayItem: ImageView =
            itemView.findViewById(R.id.imageViewForecastWeatherPictureDayItem)
        private val txtWeatherDescription: TextView =
            itemView.findViewById(R.id.textViewCurrentWeatherDayItem)
        private val txtTempDayItem: TextView =
            itemView.findViewById(R.id.textViewTemperatureDayItem)

        internal fun setDayDetails(info: Info) {
            txtDayOfWeek.text = getDayOfWeek(info.dt_txt.dropLast(9))
            txtHour.text = DataProviderManager.getTime(info.dt_txt)
            txtWeatherDescription.text = info.weather[0].description.capitalize()
            DataProviderManager.chooseImage(imgPicDayItem, info.weather[0].main, DataProviderManager.getTime(info.dt_txt))
            txtTempDayItem.text = (info.main.temp - 273.15).roundToInt().toString() + "°"
        }

    }

    internal class HoursViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtHours: TextView = itemView.findViewById(R.id.textViewHours)
        private val imgWeatherPic: ImageView =
            itemView.findViewById(R.id.imageViewForecastWeatherPicture)
        private val txtCurrentWeather: TextView =
            itemView.findViewById(R.id.textViewCurrentWeather)
        private val txtTemperature: TextView =
            itemView.findViewById(R.id.textViewTemperature)
        private val viewLine: View = itemView.findViewById(R.id.viewBottomLine)

        internal fun setHoursDetails(info: Info) {
            txtHours.text = DataProviderManager.getTime(info.dt_txt)
            txtTemperature.text = (info.main.temp - 273.15).roundToInt().toString() + "°"
            txtCurrentWeather.text = info.weather[0].description.capitalize()
            DataProviderManager.chooseImage(imgWeatherPic, info.weather[0].main, DataProviderManager.getTime(info.dt_txt))
            viewLine.isVisible = DataProviderManager.getTime(info.dt_txt) != "21:00"

        }
    }

    override fun getItemViewType(position: Int): Int {

        if (position + 1 < fiveDayInfoList.size && fiveDayInfoList[position + 1].dt_txt.drop(11)
                .dropLast(6) == "03"
        ) {
            return TYPE_DAY
        } else {
            return TYPE_HOURS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        return if (viewType == TYPE_DAY) { // for day layout
            view = LayoutInflater.from(context).inflate(R.layout.forecast_day_item, parent, false)
            DayViewHolder(view)
        } else { // for hours layout
            view = LayoutInflater.from(context).inflate(R.layout.forecast_hours_item, parent, false)
            HoursViewHolder(view)
        }
    }


    override fun getItemCount() = fiveDayInfoList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (getItemViewType(position) == TYPE_DAY) {
            (holder as DayViewHolder).setDayDetails(fiveDayInfoList[position])
        } else {
            (holder as HoursViewHolder).setHoursDetails(fiveDayInfoList[position])
        }


    }


}


fun getDayOfWeek(data: String): String {
    val date: Date = SimpleDateFormat("yyyy-MM-dd").parse(data)
    val dayFormate: DateFormat = SimpleDateFormat("EEEE")
    return dayFormate.format(date)
}