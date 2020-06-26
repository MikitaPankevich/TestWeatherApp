package com.demo.testweatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.demo.testweatherapp.R
import com.demo.testweatherapp.databinding.FragmentForecastBinding
import kotlinx.android.synthetic.main.fragment_forecast.*

/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentForecastBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false)
        return binding.root;
    }

    override fun onResume() {
        super.onResume()
    }
}
