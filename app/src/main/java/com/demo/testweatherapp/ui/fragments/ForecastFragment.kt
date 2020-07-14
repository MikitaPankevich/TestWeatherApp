package com.demo.testweatherapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.demo.testweatherapp.R
import com.demo.testweatherapp.mvp.presenters.ForecastPresenter
import com.demo.testweatherapp.mvp.views.ForecastView
import com.demo.testweatherapp.pojo.Base
import com.demo.testweatherapp.ui.adapters.DayInfoAdapter
import com.demo.testweatherapp.ui.screens.MainActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewFragmentForecast.layoutManager = LinearLayoutManager(this.context)
        itemsSwipeToRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
        itemsSwipeToRefresh.setColorSchemeColors(Color.WHITE)
        itemsSwipeToRefresh.setOnRefreshListener {
            (activity as MainActivity).startLocationUpdates()
            presenter.loadData()
           // Handler().postDelayed(Runnable {         },1500 )
            itemsSwipeToRefresh.isRefreshing = false
        }


    }

    override fun onResume() {
        super.onResume()
        presenter.loadData()
    }

    @ProvidePresenter
    fun providePresenter(): ForecastPresenter {
        return ForecastPresenter(this)
    }

    override fun showData(base: Base) {
        adapter = DayInfoAdapter(this.context, base.list)
        recyclerViewFragmentForecast.adapter = adapter
    }
}
