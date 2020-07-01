package com.demo.testweatherapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.demo.testweatherapp.R
import com.demo.testweatherapp.data.DataProviderManager
import com.demo.testweatherapp.databinding.ActivityMainBinding
import com.demo.testweatherapp.data.WeatherPresenter
import com.demo.testweatherapp.data.WeatherView
import com.demo.testweatherapp.location.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    WeatherView {


    private var LOCATION_REQUEST = 100

    private var isStartFragment = true
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var navController: NavController
    private lateinit var presenter: WeatherPresenter


    private var mLastLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        binding.imageViewForecast.setOnClickListener {
            navController.navigate(R.id.action_todayFragment_to_forecastFragment)
            changeFragment()

        }
        binding.imageViewToday.setOnClickListener {
            navController.navigate(R.id.action_forecastFragment_to_todayFragment)
            changeFragment()
        }

        presenter = WeatherPresenter(this)
        imageViewToday.isClickable = false
        imageViewForecast.isClickable = false
    }


    private fun changeFragment() {
        if (isStartFragment) {
            textViewTodayOrCity.text = DataProviderManager.base?.city?.name
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

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST
        )
    }

    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result
                    presenter.loadData(mLastLocation!!.latitude, mLastLocation!!.longitude)
                } else {
                    Log.d("TEST_ERROOR", task.result.toString())
                    Toast.makeText(
                        this,
                        "Please, turn on Location and try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            showToast("Access to geolocation is necessary for the app efficiency.",
                android.R.string.ok,
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        } else {
            startLocationPermissionRequest()
        }
    }


    private fun showToast(
        mainTextStringId: String, actionStringId: Int,
        listener: View.OnClickListener
    ) {
        val contextView = findViewById<View>(R.id.constLayoutMainActivity)
        Snackbar.make(contextView, mainTextStringId, Snackbar.LENGTH_INDEFINITE)
            .setAction("Try again") {
                listener
            }
            .show()
    }

    override fun showData() {
        imageViewForecast.isClickable = true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.disposeDisposable()
    }
}



