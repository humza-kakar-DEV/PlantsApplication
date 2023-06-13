package com.example.plantsservicefyp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.ActivityMainBinding
import com.example.plantsservicefyp.fragment.admin.ApprovalFragment
import com.example.plantsservicefyp.fragment.auth.ContainerAuthenticationFragment
import com.example.plantsservicefyp.fragment.buyer.ContainerMainData
import com.example.plantsservicefyp.fragment.seller.SellPlantFragment
import com.example.plantsservicefyp.fragment.buyer.ShowPlantFragment
import com.example.plantsservicefyp.fragment.auth.WelcomeFragment
import com.example.plantsservicefyp.fragment.buyer.PaymentFragment
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.SharedViewModel
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
                ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT -> {
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
                            ContainerAuthenticationFragment()
                        )
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.SHOW_PLANT_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                        .replace(binding.activityMainFrameLayout.id, ShowPlantFragment())
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.ADMIN_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                        .replace(binding.activityMainFrameLayout.id, ApprovalFragment())
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.SELLER_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                        .replace(binding.activityMainFrameLayout.id, SellPlantFragment())
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.BUYER_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                        .replace(binding.activityMainFrameLayout.id, ContainerMainData())
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.PAYMENT_FRAGMENT -> {
                    this.log("payment when statement called")
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                        .replace(binding.activityMainFrameLayout.id, PaymentFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

    }
}