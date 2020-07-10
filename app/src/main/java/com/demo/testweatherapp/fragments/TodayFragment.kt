package com.demo.testweatherapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.demo.testweatherapp.CardinalDirections
import com.demo.testweatherapp.R
import com.demo.testweatherapp.data.DataProviderManager
import kotlinx.android.synthetic.main.fragment_today.*
import kotlin.math.roundToInt

class TodayFragment : Fragment() {

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
        showData()
        shareData()
    }

    private fun showData() {
        DataProviderManager.base?.let {
            val description: String? = it.list[0].weather[0].description
            DataProviderManager.chooseImage(imageViewWeatherPicture, it.list[0].weather[0].main, DataProviderManager.getTime(it.list[0].dt_txt))
            if (it.city.name != null && it.city.country != null) textViewCityAndCountry.text = "%s, %s".format(it.city.name, it.city.country)
            else textViewCityAndCountry.text = getString(R.string.unknown)

            textViewTemperatureAndWeather.text = "%s°C | %s".format((it.list[0].main.temp - 273.15).roundToInt().toString(), description?.capitalize())
            textViewHumidity.text = "%s %s".format(it.list[0].main.humidity.toString(), "%")

            if (it.list[0].rain != null) textViewRainfall.text = getString(R.string.rainfall).format(it.list[0].rain.humidity.toString())
            else textViewRainfall.text = getString(R.string.noRainfall)
            textViewPressure.text = getString(R.string.pressure).format(it.list[0].main.pressure.toString())
            textViewSpeedOfWind.text = getString(R.string.speedOfWind).format((it.list[0].wind.speed * 3.6).toInt().toString())
            textViewCompassDirection.text = chooseDirection(it.list[0].wind.deg)
        }
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
