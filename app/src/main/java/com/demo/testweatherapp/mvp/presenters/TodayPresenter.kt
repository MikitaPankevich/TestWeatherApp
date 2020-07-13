package com.demo.testweatherapp.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.mvp.views.TodayView

@InjectViewState
class TodayPresenter(private val view: TodayView) : MvpPresenter<TodayView>() {
    fun getData(){
        if (DataProviderManager.base != null){
            DataProviderManager.base?.let { view.showData(it) }
        } else {
            view.showError("Error")
        }
    }

    fun isDataNull(){
        if (DataProviderManager.base == null){
        } else{
        }
    }
}