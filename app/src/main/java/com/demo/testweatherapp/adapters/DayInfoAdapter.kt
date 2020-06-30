package com.demo.testweatherapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.demo.testweatherapp.R
import com.demo.testweatherapp.pojo.Info
import com.demo.testweatherapp.screens.DataProviderManager
import java.util.*
import kotlin.math.roundToInt


class DayInfoAdapter(context: Context?, info: List<Info>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context? = context
    private var index = 0

    private val TYPE_DAY = 1
    private val TYPE_HOURS = 2

    private var fiveDayInfoList : List<Info> = info
        set(value) {
            field = value
            notifyDataSetChanged()
        }





    internal class DayViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtDayOfWeek : TextView = itemView.findViewById<TextView>(R.id.textViewDayOfWeek)

        internal fun setDayDetails(info: Info) {
           // val sdf = SimpleDateFormat("EEEE")
          //  val d = Date()
            //val currentDayOfTheWeek: String = sdf.format(d)
            var millis = info.dt.toLong()
            var time : Calendar = Calendar.getInstance()
            time.timeInMillis = millis;
            var dayOfWeek = time.get(Calendar.DAY_OF_WEEK)
            //if (dayOfWeek == currentDay){
           // holder.tvDayOfWeek.text = R.string.today.toString()
       // } else {
            Log.d("TEST_OF_ADAPTER", info.toString())
            when(dayOfWeek){
                Calendar.MONDAY -> txtDayOfWeek.text = "MONDAY"
                Calendar.TUESDAY -> txtDayOfWeek.text = "TUESDAY"
                Calendar.WEDNESDAY -> txtDayOfWeek.text = "WEDNESDAY"
                Calendar.THURSDAY -> txtDayOfWeek.text = "THURSDAY"
                Calendar.FRIDAY -> txtDayOfWeek.text = "FRIDAY"
                Calendar.SATURDAY -> txtDayOfWeek.text = "SATURDAY"
                Calendar.SUNDAY -> txtDayOfWeek.text = "SUNDAY"
            }


        }

    }

    internal class HoursViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtHours : TextView = itemView.findViewById<TextView>(R.id.textViewHours)
        private val imgWeatherPic : ImageView = itemView.findViewById<ImageView>(R.id.imageViewForecastWeatherPicture)
        private val txtCurrentWeather : TextView = itemView.findViewById<TextView>(R.id.textViewCurrentWeather)
        private val txtTemperature : TextView = itemView.findViewById<TextView>(R.id.textViewTemperature)

        internal fun setHoursDetails(info: Info) {

            txtHours.text = getTime(info.dt).toString()
            txtTemperature.text = (info.main.temp-273.15).roundToInt().toString()
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (index.equals(0)){
            return TYPE_DAY
        } else {
            return TYPE_HOURS
        }

//        val sdf = SimpleDateFormat("EEEE")
//        val d = Date()
//        val currentDayOfTheWeek: String = sdf.format(d)
//
//        var millis = fiveDayInfoList[0].dt.toLong()
//
//        var time : Calendar = Calendar.getInstance()
//        time.timeInMillis = millis;
//        var dayOfWeek = time.get(Calendar.)
//        if (currentDayOfTheWeek == dayOfWeek)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
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
            (holder as DayViewHolder).setDayDetails(fiveDayInfoList.get(position))
        } else {
            (holder as HoursViewHolder).setHoursDetails(fiveDayInfoList.get(position))
        }






//        val c : Calendar = Calendar.getInstance()
//        val currentDay = c[Calendar.DAY_OF_WEEK]
//        c.timeInMillis = fiveDayInfoList[position].dt.toLong()
//        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
//        if (dayOfWeek == currentDay){
//            holder.tvDayOfWeek.text = R.string.today.toString()
//        } else {
//            when(dayOfWeek){
//                Calendar.MONDAY -> holder.tvDayOfWeek.text = "MONDAY"
//                Calendar.TUESDAY -> holder.tvDayOfWeek.text = "TUESDAY"
//                Calendar.WEDNESDAY -> holder.tvDayOfWeek.text = "WEDNESDAY"
//                Calendar.THURSDAY -> holder.tvDayOfWeek.text = "THURSDAY"
//                Calendar.FRIDAY -> holder.tvDayOfWeek.text = "FRIDAY"
//                Calendar.SATURDAY -> holder.tvDayOfWeek.text = "SATURDAY"
//                Calendar.SUNDAY -> holder.tvDayOfWeek.text = "SUNDAY"
//            }
//        }


    }
}

fun getTime(date: Int) : Int {

        val c : Calendar = Calendar.getInstance()
        c.timeInMillis = date.toLong()
    return c[Calendar.HOUR_OF_DAY]

}