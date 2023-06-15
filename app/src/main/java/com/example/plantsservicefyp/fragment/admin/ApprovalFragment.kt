package com.example.plantsservicefyp.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.adapter.ApprovalRecyclerViewAdapter
import com.example.plantsservicefyp.adapter.CartRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentApprovalBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.ApprovalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApprovalFragment : Fragment() {

    private lateinit var binding: FragmentApprovalBinding

    private val approvalViewModel: ApprovalViewModel by viewModels()

    private lateinit var approvalRecyclerViewAdapter: ApprovalRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApprovalBinding.inflate(layoutInflater, container, false);

        approvalRecyclerViewAdapter = ApprovalRecyclerViewAdapter(requireContext()) {
        }

        approvalViewModel._observeAllPlants.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {

                }
                is UiState.Success -> {
                    it.data?.let {
                        if (it.isNotEmpty()) {
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

        return binding.root
    }
}