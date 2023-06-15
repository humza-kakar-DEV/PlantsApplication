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
import com.example.plantsservicefyp.databinding.FragmentSignUpBinding
import com.example.plantsservicefyp.model.firebase.User
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.constant.FirebaseAuthRolesConstants
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.flod.loadingbutton.LoadingButton.OnStatusChangedListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var usernameInput: String
    private lateinit var phoneNumber: String
    private lateinit var emailInput: String
    private lateinit var passwordInput: String
    private lateinit var authRole: FirebaseAuthRolesConstants

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Admin -> {
                    requireContext().toast("admin role")
                    loadingComplete(ChangeFragment.ADMIN_FRAGMENT)
                }
                is CurrentUserType.Buyer -> {
                    requireContext().toast("buyer role")
                    requireContext().log("sign up fragment: buyer role")
                    loadingComplete(ChangeFragment.BUYER_FRAGMENT)
                }
                is CurrentUserType.Seller -> {
                    requireContext().toast("seller role")
                    loadingComplete(ChangeFragment.SELLER_FRAGMENT)
                }
                CurrentUserType.Loading -> {
                    requireContext().toast("current user loading")
                }
                is CurrentUserType.Exception -> {
                    requireContext().toast("exception ${it.error.toString()}")
                    binding.signUpLoadingButton.complete(false)
                }
            }
        }

        binding.signUpSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            when (newItem) {
                "admin" -> {
                    context?.toast("admin")
                    authRole = FirebaseAuthRolesConstants.FIRESTORE_ADMIN
                }
                "buyer" -> {
                    context?.toast("buyer")
                    authRole = FirebaseAuthRolesConstants.FIRESTORE_BUYER
                }
                "seller" -> {
                    context?.toast("seller")
                    authRole = FirebaseAuthRolesConstants.FIRESTORE_SELLER
                }
            }
        }

        authenticationViewModel._observeSignUp.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    Log.d("hm123", "signup -> Loading")
                    binding.signUpLoadingButton.start()
                }
                is UiState.Success -> {
                    requireContext().log("sign up fragment: success")
                    authenticationViewModel.currentUser()
                }
                is UiState.Exception -> {
                    binding.signUpLoadingButton.complete(false)
                    Log.d("hm123", "signup -> error: ${it.message}")
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signUpLoadingButton.setOnClickListener returnOnClick@{
            if (!validateEmail() or !validatePassword() or !validateUsername() or !validatePhoneNumber()) {
                return@returnOnClick
            }
            User(
                name = usernameInput,
                phoneNumber = phoneNumber,
                email = emailInput,
                password = passwordInput
            ).apply {
                authenticationViewModel.signUp(
                    firebaseAuthRolesConstants = authRole,
                    user = this
                )
            }
        }

        return binding.root
    }

    private fun loadingComplete (changeFragment: ChangeFragment) {
        binding.signUpLoadingButton.complete(true)
        binding.signUpLoadingButton.setOnStatusChangedListener(object :
            OnStatusChangedListener() {
            override fun onRestored() {
                super.onRestored()
                sharedViewModel.changeFragment(changeFragment)
            }
        })
    }

    private fun validateUsername(): Boolean {
        usernameInput = binding.textInputName.getEditText()?.getText().toString().trim()
        return if (usernameInput.isEmpty()) {
            binding.textInputName.setError("Field can't be empty")
            false
        } else if (usernameInput.length > 15) {
            binding.textInputName.setError("Username too long")
            false
        } else {
            binding.textInputName.setError(null)
            true
        }
    }

    private fun validatePhoneNumber(): Boolean {
        phoneNumber = binding.textInputMobileNumber.getEditText()?.getText().toString().trim()
        return if (phoneNumber.isEmpty()) {
            binding.textInputMobileNumber.setError("Field can't be empty")
            false
        } else {
            binding.textInputMobileNumber.setError(null)
            true
        }
    }

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