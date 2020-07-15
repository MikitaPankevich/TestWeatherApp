package com.demo.testweatherapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.demo.testweatherapp.R
import com.demo.testweatherapp.common.Utils
import com.demo.testweatherapp.mvp.presenters.TodayPresenter
import com.demo.testweatherapp.mvp.views.TodayView
import com.demo.testweatherapp.pojo.Base
import kotlinx.android.synthetic.main.fragment_today.*
import kotlin.math.roundToInt


class TodayFragment : MvpAppCompatFragment(), TodayView {

    @InjectPresenter
    internal lateinit var presenter : TodayPresenter

    private var intent: Intent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    private fun shareData() {
        val textViewShare = this.view?.findViewById<TextView>(R.id.textViewShare)
        textViewShare?.setOnClickListener {
            presenter.createShareIntent()
            startActivity( Intent.createChooser(intent , null) )
        }
    }

    @ProvidePresenter
    fun providePresenter(): TodayPresenter {
        return TodayPresenter(this)
    }


    override fun onResume() {
        super.onResume()
        presenter.isDataNull()
        presenter.getData()
        shareData()
    }

    override fun showScreen(){
        if (progressBar != null && titleConstraintToday != null ){
            progressBar.visibility = View.GONE
            titleConstraintToday.visibility = View.VISIBLE
        }
    }

    override fun createShareIntent(base: Base) {
         intent = Intent().apply {
            action = Intent.ACTION_SEND
            base.let {
                putExtra(
                    Intent.EXTRA_TEXT, String.format( getString(R.string.textToShare) ,
                        it.city.name.capitalize() , (it.list[0].main.temp - 273.15).roundToInt().toString(), it.list[0].main.humidity.toString(), it.list[0].wind.speed.roundToInt().toString() ))}
            type = getString(R.string.typeToShare)
        }
    }

    override fun hideScreen(){
        progressBar.visibility = View.VISIBLE
        titleConstraintToday.visibility = View.GONE
    }


    override fun showData(base: Base) {
       with(base) {
            val description: String? = list[0].weather[0].description
            Utils.chooseImage(imageViewWeatherPicture, list[0].weather[0].main, Utils.getTime(list[0].dt_txt))
            if (city.name != null && city.country != null) textViewCityAndCountry.text = "%s, %s".format(city.name, city.country)
            else textViewCityAndCountry.text = getString(R.string.unknown)
            textViewTemperatureAndWeather.text = "%s°C | %s".format((list[0].main.temp - 273.15).roundToInt().toString(), description?.capitalize())
            textViewHumidity.text = "%s %s".format(list[0].main.humidity.toString(), "%")
            if (list[0].rain != null) textViewRainfall.text = getString(R.string.rainfall).format(list[0].rain.humidity.toString())
            else textViewRainfall.text = getString(R.string.noRainfall)
            textViewPressure.text = getString(R.string.pressure).format(list[0].main.pressure.toString())
            textViewSpeedOfWind.text = getString(R.string.speedOfWind).format((list[0].wind.speed * 3.6).toInt().toString())
            textViewCompassDirection.text = Utils.chooseDirection(list[0].wind.deg)
        }
    }
}
