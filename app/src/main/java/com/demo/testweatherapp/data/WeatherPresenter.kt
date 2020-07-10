package com.demo.testweatherapp.data

import com.demo.testweatherapp.api.ApiFactory
import com.demo.testweatherapp.pojo.Base
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WeatherPresenter(private val view: WeatherView) {

    private val compositeDisposable = CompositeDisposable()
    lateinit var data: Base

    fun loadData(latitude: Double, longitude: Double) {
        val disposable = ApiFactory.apiService.getForecast(lat = latitude, lon = longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DataProviderManager.registerDataProvider(it)
                view.showData()
            }, {
                view.showError()
            })
        compositeDisposable.add(disposable)
    }
    
    fun disposeDisposable() {
        compositeDisposable.dispose()
    }


}

