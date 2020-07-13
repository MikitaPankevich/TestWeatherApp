package com.demo.testweatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.demo.testweatherapp.R
import com.demo.testweatherapp.mvp.presenters.ForecastPresenter
import com.demo.testweatherapp.mvp.views.ForecastView
import com.demo.testweatherapp.pojo.Base
import com.demo.testweatherapp.ui.adapters.DayInfoAdapter
import kotlinx.android.synthetic.main.fragment_forecast.*


/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : MvpAppCompatFragment(), ForecastView {

    private lateinit var adapter: DayInfoAdapter

    @InjectPresenter
    internal lateinit var presenter : ForecastPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadData()
    }

    @ProvidePresenter
    fun providePresenter(): ForecastPresenter {
        return ForecastPresenter(this)
    }

    override fun showError(error: String) {
        TODO("Not yet implemented")
    }

    override fun showData(base: Base) {
        recyclerViewFragmentForecast.layoutManager = LinearLayoutManager(this.context)
        adapter = DayInfoAdapter(this.context, base.list)
        recyclerViewFragmentForecast.adapter = adapter
    }
}
