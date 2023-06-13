package com.example.plantsservicefyp.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.databinding.FragmentWelcomeBinding
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment() : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)

        binding.welcomeButton.setOnClickListener {
            authenticationViewModel.currentUser()
        }

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Admin -> {
                    requireContext().toast("admin role")
                    sharedViewModel.changeFragment(ChangeFragment.ADMIN_FRAGMENT)
                }
                is CurrentUserType.Buyer -> {
                    requireContext().toast("buyer role")
                    sharedViewModel.changeFragment(ChangeFragment.BUYER_FRAGMENT)
                }
                is CurrentUserType.Seller -> {
                    requireContext().toast("seller role")
                    sharedViewModel.changeFragment(ChangeFragment.SELLER_FRAGMENT)
                }
                CurrentUserType.Loading -> {
                    requireContext().toast("current user loading")
                }
                is CurrentUserType.Exception -> {
                    requireContext().toast("current user exception ${it.error.toString()}")
                    sharedViewModel.changeFragment(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT)
                }
            }
        }

        return binding.root
    }
}