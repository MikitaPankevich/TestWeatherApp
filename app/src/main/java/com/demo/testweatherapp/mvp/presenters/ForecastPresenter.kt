package com.demo.testweatherapp.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.mvp.views.ForecastView

@InjectViewState
class ForecastPresenter (private val view: ForecastView) : MvpPresenter<ForecastView>(){
    fun loadData(){
        if (DataProviderManager.base != null){
            DataProviderManager.base?.let { view.showData(it) }
        } else {
            view.showError("Error")
        }

    }
}