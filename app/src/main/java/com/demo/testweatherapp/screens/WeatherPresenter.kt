package com.demo.testweatherapp.screens

import android.util.Log
import com.demo.testweatherapp.api.ApiFactory
import com.demo.testweatherapp.pojo.Base
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WeatherPresenter(view : WeatherView) {

    private val view = view
    private val compositeDisposable = CompositeDisposable()

    fun loadData(latitude: Double, longitude: Double) {
        val disposable = ApiFactory.apiService.getForecast(lat = latitude,lon = longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showData(it)
            },{
                view.showError()
                Log.d("Error_loadData: ", it.message)
            })
        compositeDisposable.add(disposable)
    }

    fun disposeDisposable(){
        compositeDisposable.dispose()
    }
}