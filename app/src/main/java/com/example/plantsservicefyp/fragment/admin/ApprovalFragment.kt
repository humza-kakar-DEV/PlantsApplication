package com.example.plantsservicefyp.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.adapter.ApprovalRecyclerViewAdapter
import com.example.plantsservicefyp.adapter.CartRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentApprovalBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.ApprovalViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApprovalFragment : Fragment() {

    private lateinit var binding: FragmentApprovalBinding

    private val approvalViewModel: ApprovalViewModel by viewModels()

    private lateinit var approvalRecyclerViewAdapter: ApprovalRecyclerViewAdapter

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApprovalBinding.inflate(layoutInflater, container, false);

        approvalRecyclerViewAdapter =
            ApprovalRecyclerViewAdapter(requireContext(), { statePlant ->
                approvalViewModel.editPlantState(statePlant)
            }, { detailedPlant ->
                sharedViewModel.setSelectedPlant(detailedPlant)
                sharedViewModel.changeFragment(ChangeFragment.SHOW_PLANT_DETAILED_FRAGMENT)
            })

        approvalViewModel._observePlantState.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {

                }
                is UiState.Success -> {
                    approvalViewModel.getAllPlants()
                }
                is UiState.Exception -> {

                }
            }
        }

        approvalViewModel._observeAllPlants.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {

                }
                is UiState.Success -> {
                    it.data?.let {
                        if (it.isNotEmpty()) {
                            binding.itemFoundTextView.text = "${it.size} founds"
                            approvalRecyclerViewAdapter.updateAdapterWithDocuments(it)
                        } else {
                            context?.log("plant list is empty")
                        }
                    }
                }
                is UiState.Exception -> {
                    context?.log("list error: ${it.message}")
                }
            }
        }

        binding.approvalFragmentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.approvalFragmentRecyclerView.adapter = approvalRecyclerViewAdapter

        return binding.root
    }
}