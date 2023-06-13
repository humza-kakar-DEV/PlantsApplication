package com.example.plantsservicefyp.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plantsservicefyp.databinding.FragmentApprovalBinding
import com.example.plantsservicefyp.util.toast

class ApprovalFragment : Fragment() {

    private lateinit var binding: FragmentApprovalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApprovalBinding.inflate(layoutInflater, container, false);

        return binding.root
    }
}