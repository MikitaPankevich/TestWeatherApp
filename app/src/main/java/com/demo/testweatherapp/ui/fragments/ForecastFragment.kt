package com.demo.testweatherapp.ui.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private var builder: AlertDialog.Builder? = null

    @InjectPresenter
    internal lateinit var presenter : ForecastPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeToRefresh = view.findViewById(R.id.itemsSwipeToRefresh)
        recyclerView = view.findViewById(R.id.recyclerViewFragmentForecast)
        progressBar = view.findViewById(R.id.progressBarForecast)
        builder = this.context?.let { AlertDialog.Builder(it) }
        recyclerViewFragmentForecast.layoutManager = LinearLayoutManager(this.context)
        swipeToRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
        swipeToRefresh.setColorSchemeColors(Color.WHITE)
        swipeToRefresh.setOnRefreshListener {
            (activity as MainActivity).startLocationUpdates()
            Handler().postDelayed({
                presenter.showChanges()
                presenter.loadData()
                swipeToRefresh.isRefreshing = false
            },1500 )
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.isDataNull()
        presenter.loadData()
    }

    @ProvidePresenter
    fun providePresenter(): ForecastPresenter {
        return ForecastPresenter(this)
    }

    override fun showData(base: Base) {
        adapter = DayInfoAdapter(this.context, base.list)
        recyclerView.adapter = adapter
    }
    override fun showAlert(text: MutableList<String>?) {
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            //Could be some code
        }
        val textOfMessage: String = text?.toString() ?: getString(R.string.alertNoChanges)
        builder?.let {
            with (it)
                {
                    setTitle(getString(R.string.alertTitle))
                    setMessage(textOfMessage)
                    setPositiveButton(getString(R.string.alertPositiveButtonText), DialogInterface.OnClickListener(function = positiveButtonClick))
                    show()
                }
        }
    }

    override fun hideScreen() {
        progressBar.visibility = View.VISIBLE
        swipeToRefresh.visibility = View.GONE
    }

    override fun showScreen() {
        if (progressBarForecast != null && itemsSwipeToRefresh != null ){
            progressBar.visibility = View.GONE
            swipeToRefresh.visibility = View.VISIBLE
        }
    }
}
