package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentDisplayReminderPlantBinding

class DisplayReminderPlant : Fragment() {

    private lateinit var binding: FragmentDisplayReminderPlantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisplayReminderPlantBinding.inflate(layoutInflater, container, false)



        return binding.root
    }
}