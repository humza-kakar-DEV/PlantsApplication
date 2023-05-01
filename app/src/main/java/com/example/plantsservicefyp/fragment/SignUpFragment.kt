package com.example.plantsservicefyp.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.plantsservicefyp.model.User
import com.example.plantsservicefyp.util.ChangeFragment
import com.example.plantsservicefyp.util.PasswordPattern
import com.example.plantsservicefyp.util.UiState
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        authenticationViewModel._observeSignUp.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    Log.d("hm123", "signup -> Loading")
                    binding.signUpLoadingButton.start()
                }
                is UiState.Success -> {
                    Log.d("hm123", "signup -> success: ${it.data?.user?.uid}")
                    binding.signUpLoadingButton.complete(true)
                    binding.signUpLoadingButton.setOnStatusChangedListener(object :
                        OnStatusChangedListener() {
                        override fun onRestored() {
                            super.onRestored()
                            sharedViewModel.changeFragment(ChangeFragment.CONTAINER_MAIN_DATA_FRAGMENT)
                        }
                    })
                }
                is UiState.Error -> {
                    Log.d("hm123", "signup -> error: ${it.exception}")
                    Toast.makeText(context, "${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signUpLoadingButton.setOnClickListener returnOnClick@{
            if (!validateEmail() or !validatePassword() or !validateUsername() or !validatePhoneNumber()) {
                return@returnOnClick
            }
            User(
                id = "",
                name = usernameInput,
                phoneNumber = phoneNumber,
                email = emailInput,
                password = passwordInput
            ).apply {
                authenticationViewModel.signUp(user = this)
            }
        }

        return binding.root
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