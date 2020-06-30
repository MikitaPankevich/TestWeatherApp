package com.demo.testweatherapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.demo.testweatherapp.R
import com.demo.testweatherapp.databinding.ActivityMainBinding
import com.demo.testweatherapp.fragments.WeatherPresenter
import com.demo.testweatherapp.pojo.Info
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){


    private var LOCATION_REQUEST = 100

    private var currentCity: String = ""
    private var isStartFragment = true
    private lateinit var locationViewModel: LocationViewModel
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var navController : NavController




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.myNavHostFragment)
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
        startLocationUpdate()
    }


    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {

        })
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


}



