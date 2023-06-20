package com.example.plantsservicefyp.fragment.auth

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.R
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.databinding.FragmentWelcomeBinding
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.flod.loadingbutton.LoadingButton
import com.vmadalin.easypermissions.EasyPermissions
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

        binding.welcomeButton.setOnClickListener onClickListener@{
            if (!EasyPermissions.hasPermissions(activity, Manifest.permission.POST_NOTIFICATIONS)) {
                EasyPermissions.requestPermissions(
                    host = this,
                    rationale = "Enable notification permission for better user experience!",
                    requestCode = 1101,
                    perms = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                )
                return@onClickListener
            }

            authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
                when (it) {
                    is CurrentUserType.Admin -> {
                        Thread(Runnable {
                            Handler(
                                Looper.getMainLooper()
                            ).postDelayed(
                                Runnable {
                                    binding.welcomeButton.complete(true)
                                }, 1500
                            )
                        }).start()
                        binding.welcomeButton.setOnStatusChangedListener(object :
                            LoadingButton.OnStatusChangedListener() {
                            override fun onRestored() {
                                super.onRestored()
                                sharedViewModel.changeFragment(ChangeFragment.ADMIN_FRAGMENT)
                            }
                        })
                    }
                    is CurrentUserType.Buyer -> {
                        Thread(Runnable {
                            Handler(
                                Looper.getMainLooper()
                            ).postDelayed(
                                Runnable {
                                    binding.welcomeButton.complete(true)
                                }, 1500
                            )
                        }).start()
                        binding.welcomeButton.setOnStatusChangedListener(object :
                            LoadingButton.OnStatusChangedListener() {
                            override fun onRestored() {
                                super.onRestored()
                                sharedViewModel.changeFragment(ChangeFragment.BUYER_FRAGMENT)
                            }
                        })
                    }
                    is CurrentUserType.Seller -> {
                        Thread(Runnable {
                            Handler(
                                Looper.getMainLooper()
                            ).postDelayed(
                                Runnable {
                                    binding.welcomeButton.complete(true)
                                }, 1500
                            )
                        }).start()
                        binding.welcomeButton.setOnStatusChangedListener(object :
                            LoadingButton.OnStatusChangedListener() {
                            override fun onRestored() {
                                super.onRestored()
                                sharedViewModel.changeFragment(ChangeFragment.SELLER_FRAGMENT)
                            }
                        })
                    }
                    CurrentUserType.Loading -> {
                        binding.welcomeButton.start()
                    }
                    is CurrentUserType.Exception -> {
                        binding.welcomeButton.start()
                        Thread(Runnable {
                            Handler(
                                Looper.getMainLooper()
                            ).postDelayed(
                                Runnable {
                                    binding.welcomeButton.complete(true)
                                }, 1500
                            )
                        }).start()
                        context?.toast("user not signed in")
                        binding.welcomeButton.setOnStatusChangedListener(object :
                            LoadingButton.OnStatusChangedListener() {
                            override fun onRestored() {
                                super.onRestored()
                                sharedViewModel.changeFragment(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT)
                            }
                        })
                    }
                }
            }

        }

        return binding.root
    }

}