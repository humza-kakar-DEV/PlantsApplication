package com.example.plantsservicefyp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.SpecialOfferRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentSearchBinding
import com.example.plantsservicefyp.databinding.FragmentSeeAllBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private var data = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        for (i in 1..49)
            data.add("Robbin plant ${i}")

        binding.searchFragmentRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

//        binding.searchFragmentRecyclerView.adapter =
//            SpecialOfferRecyclerViewAdapter(requireContext(), data)

        return binding.root
    }
}