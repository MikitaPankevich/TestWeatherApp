package com.demo.testweatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.demo.testweatherapp.R
import com.demo.testweatherapp.databinding.FragmentTodayBinding

/**
 * A simple [Fragment] subclass.
 */
class TodayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTodayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)
        return binding.root;
    }

}
