package com.demo.testweatherapp.mvp.presenters

import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.mvp.views.TodayView

@InjectViewState
class TodayPresenter(private val view: TodayView) : MvpPresenter<TodayView>() {
    fun getData(){
        if (DataProviderManager.base != null){
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

    fun createShareIntent() {
        if (DataProviderManager.base != null){
            DataProviderManager.base?.let {
                view.createShareIntent(it)
            }
        }
    }
}