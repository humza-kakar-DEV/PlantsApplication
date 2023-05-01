package com.example.plantsservicefyp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.plantsservicefyp.activity.MainActivity
import com.example.plantsservicefyp.databinding.FragmentWelcomeBinding
import com.example.plantsservicefyp.util.ChangeFragment
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.currentCoroutineContext

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
                UiState.FirebaseAuthState.LoggedIn -> {
                    sharedViewModel.changeFragment(ChangeFragment.CONTAINER_MAIN_DATA_FRAGMENT)
                }
                UiState.FirebaseAuthState.LoggedOut -> {
                    sharedViewModel.changeFragment(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT)
                }
            }
        }

        return binding.root
    }
}