package com.demo.testweatherapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.demo.testweatherapp.R
import com.demo.testweatherapp.common.CardinalDirections
import com.demo.testweatherapp.common.Utils
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.mvp.presenters.TodayPresenter
import com.demo.testweatherapp.mvp.views.TodayView
import com.demo.testweatherapp.pojo.Base
import kotlinx.android.synthetic.main.fragment_today.*
import kotlin.math.roundToInt


class TodayFragment : MvpAppCompatFragment(), TodayView {

    @InjectPresenter
    internal lateinit var presenter : TodayPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    private fun shareData() {
        val textViewShare = this.view?.findViewById<TextView>(R.id.textViewShare)
        textViewShare?.setOnClickListener {
            if (DataProviderManager.base != null) {
                startActivity( Intent.createChooser(getShareIntent(), null) )
            }
        }
    }

    @ProvidePresenter
    fun providePresenter(): TodayPresenter {
        return TodayPresenter(this)
    }

    private fun getShareIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            DataProviderManager.base?.let {
                putExtra(
                    Intent.EXTRA_TEXT, String.format( getString(R.string.textToShare) ,
                        it.city.name.capitalize() , (it.list[0].main.temp - 273.15).roundToInt().toString(), it.list[0].main.humidity.toString(), it.list[0].wind.speed.roundToInt().toString() ))}
            type = getString(R.string.typeToShare)
       }
    }

    override fun onResume() {
        super.onResume()
        if (DataProviderManager.base != null){
            progressBar.visibility = View.GONE
            titleConstraintToday.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.VISIBLE
            titleConstraintToday.visibility = View.GONE
        }
        presenter.getData()
        shareData()
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
            textViewCompassDirection.text = chooseDirection(list[0].wind.deg)
        }
    }

    override fun showError(error: String) {
        Toast.makeText(this.context, error, Toast.LENGTH_SHORT).show()
    }

    private fun chooseDirection(degree: Int): String{
        return when (degree) {
            in 23..68 -> CardinalDirections.NE
            in 69..114 -> CardinalDirections.E
            in 115..160 -> CardinalDirections.SE
            in 161..206 -> CardinalDirections.S
            in 207..252 -> CardinalDirections.SW
            in 253..298 -> CardinalDirections.W
            in 299..344 -> CardinalDirections.NW
            else -> CardinalDirections.N
        }.toString()
    }
}
