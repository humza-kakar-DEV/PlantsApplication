package com.example.plantsservicefyp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plantsservicefyp.databinding.FragmentBuyPlantBinding

class BuyPlantFragment : Fragment() {

    private lateinit var binding: FragmentBuyPlantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyPlantBinding.inflate(inflater, container, false)



        return binding.root
    }
}