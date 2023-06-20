package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.plantsservicefyp.adapter.PlantItemsRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentHomeBinding
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.HomeViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment() : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    lateinit var plantItemsRecyclerViewAdapter: PlantItemsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.getApprovedPlants()

        plantItemsRecyclerViewAdapter =
            PlantItemsRecyclerViewAdapter(requireContext()) { selectedPlant ->
                sharedViewModel.changeFragment(ChangeFragment.SHOW_PLANT_FRAGMENT)
                sharedViewModel.setSelectedPlant(selectedPlant)
            }

        sharedViewModel._observeDestroyFragment.observe(viewLifecycleOwner) {
            if (it==true) {
                context?.toast("home fragment on destroy called!")
                context?.log("home fragment on destroy called!")
                onDestroy()
            }
        }

        homeViewModel.observeSearchPlantList.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.itemFoundTextView.text = "0 founds"
                }
                is UiState.Success -> {
                    it.data?.let {
                        if (it.isNotEmpty()) {
                            binding.itemFoundTextView.text = "${it.size.toString()} founds"
                            plantItemsRecyclerViewAdapter.updateAdapterWithDocuments(it)
                        } else {
//                            plant document list is empty -> update ui
                            plantItemsRecyclerViewAdapter.updateAdapterWithDocuments(emptyList())
                        }
                    }
                }
                is UiState.Exception -> {
                    Log.d("hm123", "see all fragment error -> ${it.message}")
                }
            }
        }

        binding.seeAllFragmentRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.seeAllFragmentRecyclerView.adapter = plantItemsRecyclerViewAdapter

        binding.allChip.isChecked = true
        binding.allChip.setOnClickListener {
            homeViewModel.getApprovedPlants()
        }

        binding.herbsChip.setOnClickListener {
            homeViewModel.searchByCategory("Herbs")
        }

        binding.climbersChip.setOnClickListener {
            homeViewModel.searchByCategory("Climbers")
        }

        binding.creepersChip.setOnClickListener {
            homeViewModel.searchByCategory("Creepers")
        }

        binding.shrubsChip.setOnClickListener {
            homeViewModel.searchByCategory("Shrubs")
        }

        binding.treesChip.setOnClickListener {
            homeViewModel.searchByCategory("Trees")
        }

        return binding.root
    }
}