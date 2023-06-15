package com.example.plantsservicefyp.fragment.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.databinding.FragmentSignInBinding
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.flod.loadingbutton.LoadingButton.OnStatusChangedListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var emailInput: String
    private lateinit var passwordInput: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Admin -> {
                    requireContext().toast("admin role")
                    loadingComplete(ChangeFragment.ADMIN_FRAGMENT)
                }
                is CurrentUserType.Buyer -> {
                    requireContext().toast("buyer role")
                    loadingComplete(ChangeFragment.BUYER_FRAGMENT)
                }
                is CurrentUserType.Seller -> {
                    requireContext().toast("seller role")
                    loadingComplete(ChangeFragment.SELLER_FRAGMENT)
                }
                is CurrentUserType.Exception -> {
                    requireContext().toast("current user exception ${it.error.toString()}")
                }
                CurrentUserType.Loading -> {
                    requireContext().toast("current user loading")
                }
            }
        }

        authenticationViewModel._observeSignIn.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    Log.d("hm123", "sign in -> Loading")
                    binding.signInLoadingButton.start()
                }
                is UiState.Success -> {
                    Log.d("hm123", "signin -> success: ${it.data?.user?.uid}")
                    binding.signInLoadingButton.complete(true)
                    binding.signInLoadingButton.setOnStatusChangedListener(object :
                        OnStatusChangedListener() {
                        override fun onRestored() {
                            super.onRestored()
                            authenticationViewModel.currentUser()
                        }
                    })
                }
                is UiState.Exception -> {
                    Log.d("hm123", "sign in -> error: ${it.message}")
                    Toast.makeText(requireContext(), "error: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                    binding.signInLoadingButton.complete(false)
                }
            }
        }

        binding.signInLoadingButton.setOnClickListener returnOnClick@{
            if (!validateEmail() or !validatePassword()) {
                return@returnOnClick
            }
            authenticationViewModel.signIn(emailInput, passwordInput)
        }

        return binding.root
    }

    private fun loadingComplete (changeFragment: ChangeFragment) {
        binding.signInLoadingButton.complete(true)
        binding.signInLoadingButton.setOnStatusChangedListener(object :
            OnStatusChangedListener() {
            override fun onRestored() {
                super.onRestored()
                sharedViewModel.changeFragment(changeFragment)
            }
        })
    }

    //    applying regex
    private fun validateEmail(): Boolean {
        emailInput = binding.textInputEmail.getEditText()?.getText().toString().trim()
        return if (emailInput.isEmpty()) {
            binding.textInputEmail.setError("Field can't be empty")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.textInputEmail.setError("Please enter a valid email address")
            false
        } else {
            binding.textInputEmail.setError(null)
            true
        }
    }

    private fun validatePassword(): Boolean {
        passwordInput = binding.textInputPassword.editText!!.text.toString().trim()
        return if (passwordInput.isEmpty()) {
            binding.textInputPassword.error = "Field can't be empty"
            false
        } else {
            binding.textInputPassword.error = null
            true
        }
    }
}