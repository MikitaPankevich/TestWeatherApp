package com.demo.testweatherapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.demo.testweatherapp.BuildConfig
import com.demo.testweatherapp.R
import com.demo.testweatherapp.databinding.ActivityMainBinding
import com.demo.testweatherapp.pojo.Base
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_today.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), WeatherView{


    private var currentCity: String = ""
    private var isStartFragment = true
    private lateinit var presenter: WeatherPresenter
    private lateinit var locationViewModel: LocationViewModel
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = WeatherPresenter(this)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        binding.imageViewForecast.setOnClickListener {
            Log.d("CURRENT_DEST" , navController.currentDestination?.navigatorName )
            navController.navigate(R.id.action_todayFragment_to_forecastFragment)
            changeFragment()
        }
       // androidx.navigation.fragment.FragmentNavigator$Destination@34abdd9
        binding.imageViewToday.setOnClickListener {
            Log.d("CURRENT_DEST" , navController.currentDestination?.navigatorName )
            navController.navigate(R.id.action_forecastFragment_to_todayFragment)
            changeFragment()
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdate()


    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
            presenter.loadData(it.latitude, it.longitude)
        })
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdate()
    }

    override fun showError(){
        Toast.makeText(this, "Error with loading data, please check Internet connection", Toast.LENGTH_SHORT).show()
    }

    override fun showData(base: Base?){
        base?.let {
            val description: String? = it.list[0].weather[0].description
            chooseImage(imageViewWeatherPicture, it.list[0].weather[0].main)
            if (it.city.name != null && it.city.country != null) {
                textViewCityAndCountry.text = "%s, %s".format(it.city.name, it.city.country)
            }else{
                textViewCityAndCountry.text = "Unknown"
            }
            textViewTemperatureAndWeather.text = "%s°C | %s".format((it.list[0].main.temp-273.15).roundToInt().toString() , description?.capitalize())
            textViewHumidity.text = "%s %s".format(it.list[0].main.humidity.toString(), "%")
            if (it.list[0].rain != null){
                textViewRainfall.text = "%s mm".format(it.list[0].rain.humidity.toString())
            } else {
                textViewRainfall.text = "0.0 mm"
            }
            textViewPressure.text = "%s hPa".format(it.list[0].main.pressure.toString())
            textViewSpeedOfWind.text = "%s km/h".format((it.list[0].wind.speed * 3.6).toInt().toString())
            textViewCompassDirection.text = when(it.list[0].wind.deg){
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

    private fun changeFragment(){
        if (isStartFragment){
            textViewTodayOrCity.text = currentCity
            textViewToday.setTextColor(resources.getColor(R.color.black))
            textViewForecast.setTextColor(resources.getColor(R.color.colorLightBlue))
            Picasso.get().load(R.drawable.weather_sunny_black).into(imageViewToday)
            Picasso.get().load(R.drawable.weather_partly_rainy_blue).into(imageViewForecast)
            imageViewForecast.isClickable = false
            imageViewToday.isClickable = true
            isStartFragment = false
        } else {
            textViewTodayOrCity.text = getString(R.string.today)
            textViewToday.setTextColor(resources.getColor(R.color.colorLightBlue))
            textViewForecast.setTextColor(resources.getColor(R.color.black))
            Picasso.get().load(R.drawable.weather_sunny_blue).into(imageViewToday)
            Picasso.get().load(R.drawable.weather_partly_rainy_black).into(imageViewForecast)
            imageViewForecast.isClickable = true
            imageViewToday.isClickable = false
            isStartFragment = true
        }
    }

    private fun chooseImage(imageView: ImageView, weather: String?){
        when(weather){
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

//val adapter = DayInfoAdapter()
// recyclerViewFragmentForecast.adapter = adapter
//adapter.fiveDayInfoList = it.list

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
        invokeLocationAction()
    }
}

    private fun invokeLocationAction() {
        when {
            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> Toast.makeText(this, "App requires location permission", Toast.LENGTH_SHORT)
                .show()
            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.disposeDisposable()
    }
}

const val LOCATION_REQUEST = 100
