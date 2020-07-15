package com.demo.testweatherapp.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.demo.testweatherapp.common.Utils
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.mvp.views.ForecastView
import com.demo.testweatherapp.pojo.Info
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

@InjectViewState
class ForecastPresenter (private val view: ForecastView) : MvpPresenter<ForecastView>(){

    private var listOfInfo: List<Info>? = null

    fun loadData(){
        if (DataProviderManager.base != null){
            listOfInfo = DataProviderManager.base?.list
            DataProviderManager.base?.let { view.showData(it) }
        }
    }

    fun isDataNull(){
        if (DataProviderManager.base == null){
            view.hideScreen()
        } else{
            view.showScreen()
        }
    }

    fun showChanges(){
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        var size: Int
        var changes: MutableList<String>? = null
        val newInfo = DataProviderManager.base?.list?.filter {
            Utils.getDate(it.dt_txt) == currentDate
        }
        val oldInfo = listOfInfo?.filter {
            Utils.getDate(it.dt_txt) == currentDate
        }
        if (newInfo != null && oldInfo != null) {
            size = min(oldInfo.size, newInfo.size)
            if (newInfo.size > size){
                newInfo.drop(newInfo.size-size)
            } else if (oldInfo.size > size) {
                oldInfo.drop(oldInfo.size - size)
            }
            for ((index, value) in oldInfo.withIndex() ){
                if (oldInfo[index].weather[0].main != newInfo[index].weather[0].main ||
                    oldInfo[index].main.temp != newInfo[index].main.temp){
                    changes?.add("Was: ${Utils.getTime(oldInfo[index].dt_txt)} ${oldInfo[index].weather[0].main} ${String.format("%s°",(oldInfo[index].main.temp - 273.15).roundToInt().toString())} " +
                            "\n Become: ${Utils.getTime(newInfo[index].dt_txt)} ${newInfo[index].weather[0].main} ${String.format("%s°",(newInfo[index].main.temp - 273.15).roundToInt().toString())} ")
                }
            }
        }
        view.showAlert(changes)
    }

}