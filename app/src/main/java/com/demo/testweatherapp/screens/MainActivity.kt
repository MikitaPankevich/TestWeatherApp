package com.demo.testweatherapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.demo.testweatherapp.R
import com.demo.testweatherapp.data.DataProviderManager
import com.demo.testweatherapp.data.WeatherPresenter
import com.demo.testweatherapp.data.WeatherView
import com.demo.testweatherapp.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity(), WeatherView {

    private var isStartFragment = true


    private lateinit var navController: NavController
    private lateinit var presenter: WeatherPresenter


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CHECK_SETTINGS = 100
    private val REQUEST_CHECK_RESOLUTION = 101
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = WeatherPresenter(this)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    presenter.loadData(location.latitude, location.longitude)
                    Log.d("TEST_LOC", location.toString())
                    if (navController.currentDestination?.label.toString() == "fragment_loading" && DataProviderManager.base != null) {
                        stopLocationUpdates()
                        imageViewForecast.isClickable = true
                        navController.navigate(R.id.action_loadingFragment2_to_todayFragment)
                    }
                }
            }
        }
        checkPermission()
        createLocationRequest()

        binding.imageViewForecast.setOnClickListener {
            navController.navigate(R.id.action_todayFragment_to_forecastFragment)
            changeFragment()
        }
        binding.imageViewToday.setOnClickListener {
            navController.navigate(R.id.action_forecastFragment_to_todayFragment)
            changeFragment()
        }
        imageViewToday.isClickable = false
        imageViewForecast.isClickable = false

        getCity()
    }


    fun getCity(){
        if (Geocoder.isPresent()) {
            try {
                val location = "Minsk"
                val gc = Geocoder(this)
                val addresses: List<Address> =
                    gc.getFromLocationName(location, 1) // get the found Address Objects
                val ll: MutableList<LatLng> =
                    ArrayList<LatLng>(addresses.size) // A list to save the coordinates if they are available
                for (a in addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(LatLng(a.latitude, a.longitude))
                    }
                }
                Log.d("TEST_CITY", ll.toString())
            } catch (e: IOException) {
                Log.d("TEST_CITY", e.message)
            }
        }
    }


    private fun changeFragment() {
        if (isStartFragment) {
            textViewTodayOrCity.text = DataProviderManager.base?.city?.name
            textViewToday.setTextColor(resources.getColor(R.color.black))
            textViewForecast.setTextColor(resources.getColor(R.color.colorLightBlue))
            Picasso.get().load(R.drawable.weather_today_black).into(imageViewToday)
            Picasso.get().load(R.drawable.weather_forecast_blue).into(imageViewForecast)
            imageViewForecast.isClickable = false
            imageViewToday.isClickable = true
            isStartFragment = false
        } else {
            textViewTodayOrCity.text = getString(R.string.today)
            textViewToday.setTextColor(resources.getColor(R.color.colorLightBlue))
            textViewForecast.setTextColor(resources.getColor(R.color.black))
            Picasso.get().load(R.drawable.weather_today_blue).into(imageViewToday)
            Picasso.get().load(R.drawable.weather_forecast_black).into(imageViewForecast)
            imageViewForecast.isClickable = true
            imageViewToday.isClickable = false
            isStartFragment = true
        }
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_CHECK_SETTINGS
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this, getString(R.string.locationPermission), Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_RESOLUTION -> if (resultCode == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.locationData), Toast.LENGTH_LONG).show()
            } else {
                startLocationUpdates()
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 500
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = locationRequest.let {
            LocationSettingsRequest.Builder().addLocationRequest(it)
        }
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder?.build())
        task.addOnSuccessListener {
            startLocationUpdates()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this@MainActivity, REQUEST_CHECK_RESOLUTION)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("DEBUG_CATCH", sendEx.toString())
                }
            }
        }


    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun showData() {
        //imageViewForecast.isClickable = true
    }

    override fun showError() {
        Toast.makeText(this, getString(R.string.loadDataError), Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.disposeDisposable()
    }
}



