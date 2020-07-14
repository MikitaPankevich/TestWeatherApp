package com.demo.testweatherapp.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.demo.testweatherapp.R
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.mvp.models.api.ApiFactory
import com.demo.testweatherapp.mvp.views.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


@InjectViewState
class MainPresenter(private val view: MainView) : MvpPresenter<MainView>() {

    private val compositeDisposable = CompositeDisposable()

    fun loadData(latitude: Double, longitude: Double) {
        val disposable = ApiFactory.apiService.getForecast(lat = latitude, lon = longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DataProviderManager.registerDataProvider(it)
                view.showData(it)
            }, {
                view.showError(R.string.loadDataError)
            })
        compositeDisposable.add(disposable)
    }

     fun disposeDisposable() {
        compositeDisposable.dispose()
    }

}

