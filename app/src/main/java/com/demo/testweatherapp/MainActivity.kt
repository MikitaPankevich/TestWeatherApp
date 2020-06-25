package com.demo.testweatherapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.demo.testweatherapp.adapters.DayInfoAdapter
import com.demo.testweatherapp.api.ApiFactory
import com.demo.testweatherapp.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_forecast.*

class MainActivity : AppCompatActivity() {


    private val compositeDisposable = CompositeDisposable()
    private var currentCity: String = ""
    var isStartFragment = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        loadData()
        val navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        binding.imageViewForecast.setOnClickListener {
            navController.navigate(R.id.action_todayFragment_to_forecastFragment)
            changeFragment()
        }
        binding.imageViewToday.setOnClickListener {
            navController.navigate(R.id.action_forecastFragment_to_todayFragment)
            changeFragment()
        }
    }

    fun changeFragment(){
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


    fun chooseImage(imageView: ImageView, weather: String){
        
    }


    fun loadData(){
        //val adapter = DayInfoAdapter()
       // recyclerViewFragmentForecast.adapter = adapter
        val disposable = ApiFactory.apiService.getForecast(lat = 51.5085,lon = -0.1257)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //adapter.fiveDayInfoList = it.list
                currentCity = it.city.name

                Log.d("TEST", it.toString())
            },{
                Log.d("TEST", it.message)
            })

        compositeDisposable.add(disposable)
    }





    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}
