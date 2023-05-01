package com.example.plantsservicefyp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.ActivityMainBinding
import com.example.plantsservicefyp.fragment.*
import com.example.plantsservicefyp.util.ChangeFragment
import com.example.plantsservicefyp.util.Constants
import com.example.plantsservicefyp.util.Constants.*
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .replace(
                binding.activityMainFrameLayout.id,
                WelcomeFragment()
            )
            .commit()

        sharedViewModel._observeChangeFragment.observe(this) {
            when (it) {
                ChangeFragment.CONTAINER_MAIN_DATA_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                        .replace(binding.activityMainFrameLayout.id, ContainerMainData())
                        .commit()
                }
                ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                        .replace(binding.activityMainFrameLayout.id, ContainerAuthenticationFragment())
                        .commit()
                }
            }
        }

    }
}