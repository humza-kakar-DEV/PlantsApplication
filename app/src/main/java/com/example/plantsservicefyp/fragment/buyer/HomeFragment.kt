package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.PlantItemsRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentHomeBinding
import com.example.plantsservicefyp.util.RecommendationModelAI
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.AppConstants
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.postProcessingCoefficientPlant
import com.example.plantsservicefyp.util.showAlert
import com.example.plantsservicefyp.viewmodel.HomeViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment() : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    lateinit var plantItemsRecyclerViewAdapter: PlantItemsRecyclerViewAdapter

    @Inject
    lateinit var recommendationModelAI: RecommendationModelAI

    private var isRecommendSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                recommendationModelAI.downloadModel(AppConstants.MODEL_RECOMMENDATION.value)
                recommendationModelAI.preProcess(mutableListOf())
                recommendationModelAI.postProcess(
                    intArrayOf(2),
                    floatArrayOf(1.4f),
                    mutableListOf()
                )
            }
        }

        homeViewModel.getApprovedPlants()

        plantItemsRecyclerViewAdapter =
            PlantItemsRecyclerViewAdapter(requireContext()) { selectedPlant ->
                sharedViewModel.changeFragment(ChangeFragment.SHOW_PLANT_FRAGMENT)
                sharedViewModel.setSelectedPlant(selectedPlant)
            }

        sharedViewModel._observeDestroyFragment.observe(viewLifecycleOwner) {
            if (it == true) {
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
                            requireContext().log("plant list: ${it.forEach { it.data }}")
                            if (isRecommendSelected) {
                                lifecycleScope.launch(Dispatchers.Default) {
                                    val recommendedDataSets =
                                        recommendationModelAI.loadPlantList(it, requireContext())
                                    binding.itemFoundTextView.text =
                                        "${recommendedDataSets.size} founds"
                                    plantItemsRecyclerViewAdapter.updateAdapterWithDocuments(
                                        recommendedDataSets
                                    )
                                }
                                return@let
                            }
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
            isRecommendSelected = false
            homeViewModel.getApprovedPlants()
        }

        binding.recommendationChip.setOnClickListener {
            lifecycleScope.launch {
                delay(timeMillis = 200)
            }
            requireActivity().showAlert(R.layout.model_info_alert_dialog).show()
            isRecommendSelected = true
            homeViewModel.getApprovedPlants()
        }

        binding.herbsChip.setOnClickListener {
            isRecommendSelected = false
            homeViewModel.searchByCategory("Herbs")
        }

        binding.climbersChip.setOnClickListener {
            isRecommendSelected = false
            homeViewModel.searchByCategory("Climbers")
        }

        binding.creepersChip.setOnClickListener {
            isRecommendSelected = false
            homeViewModel.searchByCategory("Creepers")
        }

        binding.shrubsChip.setOnClickListener {
            isRecommendSelected = false
            homeViewModel.searchByCategory("Shrubs")
        }

        binding.treesChip.setOnClickListener {
            isRecommendSelected = false
            homeViewModel.searchByCategory("Trees")
        }

        return binding.root
    }
}