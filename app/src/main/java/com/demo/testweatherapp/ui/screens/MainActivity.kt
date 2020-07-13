package com.demo.testweatherapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.demo.testweatherapp.R
import com.demo.testweatherapp.databinding.ActivityMainBinding
import com.demo.testweatherapp.mvp.models.DataProviderManager
import com.demo.testweatherapp.mvp.presenters.MainPresenter
import com.demo.testweatherapp.mvp.views.MainView
import com.demo.testweatherapp.pojo.Base
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : MvpAppCompatActivity(), MainView {


    private lateinit var navController: NavController

    @InjectPresenter
    internal lateinit var presenter: MainPresenter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CHECK_SETTINGS = 100
    private val REQUEST_CHECK_RESOLUTION = 101
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    presenter.loadData(location.latitude, location.longitude)
                }
            }
        }
        checkPermission()
        createLocationRequest()
        setBottomNavigationListener()
    }

    private fun setBottomNavigationListener(){
        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId) {
                R.id.page_Today -> {
                    if (navController.currentDestination?.label.toString() == "TodayFragment"){
                        false
                    }else {
                        editTextCity.isEnabled = true
                        imageViewSearch.visibility = View.VISIBLE
                        navController.navigate(R.id.action_forecastFragment_to_todayFragment)
                        true
                    }
                }
                R.id.page_Forecast -> {
                    if (navController.currentDestination?.label.toString() == "ForecastFragment"){
                        false
                    }else{
                        editTextCity.isEnabled = false
                        imageViewSearch.visibility = View.GONE
                        navController.navigate(R.id.action_todayFragment_to_forecastFragment)
                        true
                    }
                }
                else -> false
            }
        }

    }

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return MainPresenter(this)
    }

    private fun getCity(location: String){
        if (Geocoder.isPresent()) {
            try {
                val gc = Geocoder(this)
                val addresses: List<Address> = gc.getFromLocationName(location, 1)
                val ll: MutableList<LatLng> = ArrayList(addresses.size)
                for (a in addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(LatLng(a.latitude, a.longitude))
                    }
                }
                presenter.loadData(ll[0].latitude, ll[0].longitude)
                refresh()
            } catch (e: IOException) {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                Log.d("TEST_CITY", e.message)
            }
        }
    }

    private fun refresh() {
        navController.popBackStack()
        navController.navigate(R.id.todayFragment)
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
                Toast.makeText(this, getString(R.string.locationPermission), Toast.LENGTH_LONG).show()
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

    private fun checkData(){
        if (DataProviderManager.base != null) {
            stopLocationUpdates()
            refresh()
        }
    }

    override fun showData(base: Base) {
        checkData()
        var location: String = base.city.name
        editTextCity.setText(location)
        editTextCity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                location = s.toString()
            }
        })
        imageViewSearch.setOnClickListener {
            getCity(location)
            it.hideKeyboard()
            editTextCity.clearFocus()
        }
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun showError(error: Int) {
        Toast.makeText(this, getString(error), Toast.LENGTH_LONG).show()
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



