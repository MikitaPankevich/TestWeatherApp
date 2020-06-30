package com.demo.testweatherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.demo.testweatherapp.R
import com.demo.testweatherapp.databinding.FragmentTodayBinding
import com.demo.testweatherapp.pojo.Info
import com.demo.testweatherapp.screens.DataProviderManager
import com.demo.testweatherapp.screens.LocationViewModel
import com.demo.testweatherapp.screens.WeatherView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_today.*
import java.util.*
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 */
class TodayFragment : Fragment(), WeatherView {

    private  lateinit var locationViewModel: LocationViewModel
    private lateinit var presenter: WeatherPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTodayBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)
        return binding.root;
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, androidx.lifecycle.Observer {
            presenter.loadData(it.latitude, it.longitude)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        presenter = WeatherPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdate()
    }

    override fun showData() {
        Log.d("TEST_OF_SINGLE_SHOWDATA",  DataProviderManager.base?.list.toString())
        DataProviderManager.base?.let {

            val description: String? = it.list[0].weather[0].description
            chooseImage(imageViewWeatherPicture, it.list[0].weather[0].main)
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

    private fun chooseImage(imageView: ImageView, weather: String?) {
        when (weather) {
            "Clear" -> Picasso.get().load(R.drawable.weather_sunny_yellow).into(imageView)
            "Clouds" -> Picasso.get().load(R.drawable.weather_cloudy).into(imageView)
            "Drizzle" -> Picasso.get().load(R.drawable.weather_rainy).into(imageView)
            "Rain" -> Picasso.get().load(R.drawable.weather_partly_rainy_yellow).into(imageView)
            "Thunderstorm" -> Picasso.get().load(R.drawable.weather_lightning).into(imageView)
            "Snow" -> Picasso.get().load(R.drawable.weather_snowy_heavy).into(imageView)
            "Mist" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Smoke" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Haze" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Dust" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Fog" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Sand" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Ash" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Squall" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
            "Tornado" -> Picasso.get().load(R.drawable.weather_fog).into(imageView)
        }
    }
}
