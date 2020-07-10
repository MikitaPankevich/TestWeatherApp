package com.demo.testweatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.testweatherapp.R
import com.demo.testweatherapp.pojo.Info


class DayInfoAdapter(private val context: Context?, info: List<Info>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val  TYPE_DAY = 1
    private val TYPE_HOURS = 2

    private var fiveDayInfoList: List<Info> = info
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if (position + 1 < fiveDayInfoList.size && fiveDayInfoList[position + 1].dt_txt.drop(11).dropLast(6) == context?.getString(
                        R.string.timeAfterDayOfWeek)) TYPE_DAY
        else TYPE_HOURS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_DAY) DayViewHolder(LayoutInflater.from(context).inflate(R.layout.forecast_day_item, parent, false))
        else HoursViewHolder(LayoutInflater.from(context).inflate(R.layout.forecast_hours_item, parent, false))
    }

    override fun getItemCount() = fiveDayInfoList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_DAY) (holder as DayViewHolder).setDayDetails(fiveDayInfoList[position])
        else (holder as HoursViewHolder).setHoursDetails(fiveDayInfoList[position])
    }
}


