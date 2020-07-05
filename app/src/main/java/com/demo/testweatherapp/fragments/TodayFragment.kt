package com.demo.testweatherapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import com.demo.testweatherapp.R
import com.demo.testweatherapp.data.DataProviderManager
import com.demo.testweatherapp.databinding.FragmentTodayBinding
import kotlinx.android.synthetic.main.fragment_today.*
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 */
class TodayFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTodayBinding =
            inflate(inflater, R.layout.fragment_today, container, false)
        return binding.root;
    }


    private fun shareData() {
        val textViewShare = this.view?.findViewById<TextView>(R.id.textViewShare)
        textViewShare?.setOnClickListener {
            if (DataProviderManager.base != null) {
                startActivity(getShareIntent())
            }
        }
    }


    private fun getShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        DataProviderManager.base?.let {
            shareIntent.setType("text/plain").putExtra(
                Intent.EXTRA_INTENT, "\n Current weather in ${it.city.name.capitalize()} \n " +
                        "Temperature: ${(it.list[0].main.temp - 273.15).roundToInt()} \n " +
                        "Humidity: ${it.list[0].main.humidity} \n " +
                        "Speed of wind: ${it.list[0].wind.speed.roundToInt()}"
            )
        }
        return shareIntent
    }


    override fun onResume() {
        super.onResume()
        showData()
        shareData()
    }


    private fun showData() {
        DataProviderManager.base?.let {
            val description: String? = it.list[0].weather[0].description
            DataProviderManager.chooseImage(
                imageViewWeatherPicture,
                it.list[0].weather[0].main,
                DataProviderManager.getTime(it.list[0].dt_txt)
            )
            if (it.city.name != null && it.city.country != null) {
                textViewCityAndCountry.text = "%s, %s".format(it.city.name, it.city.country)
            } else {
                textViewCityAndCountry.text = "Unknown"
            }
            textViewTemperatureAndWeather.text = "%s°C | %s".format(
                (it.list[0].main.temp - 273.15).roundToInt().toString(),
                description?.capitalize()
            )
            textViewHumidity.text = "%s %s".format(it.list[0].main.humidity.toString(), "%")
            if (it.list[0].rain != null) {
                textViewRainfall.text = "%s mm".format(it.list[0].rain.humidity.toString())
            } else {
                textViewRainfall.text = "0.0 mm"
            }
            textViewPressure.text = "%s hPa".format(it.list[0].main.pressure.toString())
            textViewSpeedOfWind.text =
                "%s km/h".format((it.list[0].wind.speed * 3.6).toInt().toString())
            textViewCompassDirection.text = when (it.list[0].wind.deg) {
                in 23..68 -> "NE"
                in 69..114 -> "E"
                in 115..160 -> "SE"
                in 161..206 -> "S"
                in 207..252 -> "SW"
                in 253..298 -> "W"
                in 299..344 -> "NW"
                else -> "N"
            }
        }
    }


}
