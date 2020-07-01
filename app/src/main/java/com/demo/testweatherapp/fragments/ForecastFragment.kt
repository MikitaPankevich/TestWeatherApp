package com.demo.testweatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.testweatherapp.R
import com.demo.testweatherapp.adapters.DayInfoAdapter
import com.demo.testweatherapp.databinding.FragmentForecastBinding
import com.demo.testweatherapp.data.DataProviderManager
import kotlinx.android.synthetic.main.fragment_forecast.*


/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : Fragment() {

    private lateinit var adapter: DayInfoAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentForecastBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        recyclerViewFragmentForecast.layoutManager = LinearLayoutManager(this.context)
        adapter = DayInfoAdapter(this.context, DataProviderManager.base!!.list)
        recyclerViewFragmentForecast.adapter = adapter
    }


}
