package com.demo.testweatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.testweatherapp.R
import com.demo.testweatherapp.pojo.Info
import kotlinx.android.synthetic.main.forecast_item.view.*
import java.util.*

class DayInfoAdapter: RecyclerView.Adapter<DayInfoAdapter.DayInfoViewHolder>() {


    var fiveDayInfoList : List<Info> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class DayInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvDayOfWeek = itemView.textViewDayOfWeek
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return DayInfoViewHolder(view)
    }

    override fun getItemCount() = fiveDayInfoList.size

    override fun onBindViewHolder(holder: DayInfoViewHolder, position: Int) {
        val c : Calendar = Calendar.getInstance()
        val currentDay = c[Calendar.DAY_OF_WEEK]
        c.timeInMillis = fiveDayInfoList[position].dt.toLong()
        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == currentDay){
            holder.tvDayOfWeek.text = R.string.today.toString()
        } else {
            when(dayOfWeek){
                Calendar.MONDAY -> holder.tvDayOfWeek.text = "MONDAY"
                Calendar.TUESDAY -> holder.tvDayOfWeek.text = "TUESDAY"
                Calendar.WEDNESDAY -> holder.tvDayOfWeek.text = "WEDNESDAY"
                Calendar.THURSDAY -> holder.tvDayOfWeek.text = "THURSDAY"
                Calendar.FRIDAY -> holder.tvDayOfWeek.text = "FRIDAY"
                Calendar.SATURDAY -> holder.tvDayOfWeek.text = "SATURDAY"
                Calendar.SUNDAY -> holder.tvDayOfWeek.text = "SUNDAY"
            }
        }


    }
}