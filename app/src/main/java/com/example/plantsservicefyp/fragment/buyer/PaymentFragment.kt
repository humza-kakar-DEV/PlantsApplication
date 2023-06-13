package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentPaymentBinding
import com.example.plantsservicefyp.util.JazzCashPayment
import com.example.plantsservicefyp.viewmodel.SharedViewModel

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)

        sharedViewModel._observeTotalPrice.observe(viewLifecycleOwner) {
            JazzCashPayment(requireContext()).launch(binding.paymentFragmentWebView, it)
        }

        return binding.root
    }
}