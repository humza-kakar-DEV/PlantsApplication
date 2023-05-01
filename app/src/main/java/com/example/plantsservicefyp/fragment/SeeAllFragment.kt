package com.example.plantsservicefyp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.plantsservicefyp.adapter.SpecialOfferRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentSeeAllBinding
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.AutoFitGridLayoutManager
import com.example.plantsservicefyp.util.GridSpacingItemDecoration
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.viewmodel.SeeAllFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeeAllFragment : Fragment() {

    private lateinit var binding: FragmentSeeAllBinding

    private var allPlantList = mutableListOf<Plant>()

    private val seeAllFragmentViewModel: SeeAllFragmentViewModel by viewModels()

    lateinit var specialOfferRecyclerViewAdapter: SpecialOfferRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeeAllBinding.inflate(inflater, container, false)

        seeAllFragmentViewModel.getAllPlants()

        specialOfferRecyclerViewAdapter = SpecialOfferRecyclerViewAdapter(requireContext(), allPlantList)

        seeAllFragmentViewModel._observePlantList.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    Log.d("hm123", "see all fragment -> Loading")
                }
                is UiState.Success -> {
                    allPlantList.clear()
                    allPlantList.addAll(it.data!!)
                    specialOfferRecyclerViewAdapter.updateAdapter(allPlantList)
                }
                is UiState.Error -> {
                    Log.d("hm123", "see all fragment error -> ${it.exception}")
                }
            }
        }

        binding.seeAllFragmentRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.seeAllFragmentRecyclerView.adapter = specialOfferRecyclerViewAdapter

        return binding.root
    }

}