package com.example.plantsservicefyp.activity

import com.example.plantsservicefyp.R
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import com.example.plantsservicefyp.databinding.ActivityMainBinding
import com.example.plantsservicefyp.fragment.admin.ApprovalFragment
import com.example.plantsservicefyp.fragment.admin.ShowDetailedPlant
import com.example.plantsservicefyp.fragment.auth.ContainerAuthenticationFragment
import com.example.plantsservicefyp.fragment.auth.WelcomeFragment
import com.example.plantsservicefyp.fragment.buyer.ContainerMainData
import com.example.plantsservicefyp.fragment.buyer.PaymentFragment
import com.example.plantsservicefyp.fragment.buyer.ShowPlantFragment
import com.example.plantsservicefyp.fragment.seller.SellPlantFragment
import com.example.plantsservicefyp.util.clearBackStack
import com.example.plantsservicefyp.util.closeApplicationAlertDialog
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val welcomeFragment = WelcomeFragment()

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
                welcomeFragment,
                ChangeFragment.WELCOME_FRAGMENT.value
            )
            .addToBackStack(ChangeFragment.WELCOME_FRAGMENT.value)
            .commit()

        sharedViewModel._observeChangeFragment.observe(this) {
            when (it) {
                ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT -> {
                    supportFragmentManager.clearBackStack()
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
                            ContainerAuthenticationFragment(),
                            ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT.value
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
                        .replace(
                            binding.activityMainFrameLayout.id,
                            ShowPlantFragment(),
                            ChangeFragment.SHOW_PLANT_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.ADMIN_FRAGMENT -> {
                    supportFragmentManager.clearBackStack()
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
                            ApprovalFragment(),
                            ChangeFragment.ADMIN_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.SELLER_FRAGMENT -> {
                    supportFragmentManager.clearBackStack()
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
                            SellPlantFragment(),
                            ChangeFragment.SELLER_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.BUYER_FRAGMENT -> {
                    supportFragmentManager.clearBackStack()
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
                            ContainerMainData(),
                            ChangeFragment.BUYER_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.PAYMENT_FRAGMENT -> {
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
                            PaymentFragment(),
                            ChangeFragment.PAYMENT_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }
                ChangeFragment.SHOW_PLANT_DETAILED_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            binding.activityMainFrameLayout.id,
                            ShowDetailedPlant(),
                            ChangeFragment.SHOW_PLANT_DETAILED_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }
                else -> {}
            }
        }

    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag(ChangeFragment.BUYER_FRAGMENT.value) != null) {
            if (supportFragmentManager.findFragmentByTag(ChangeFragment.SHOW_PLANT_FRAGMENT.value) != null) {
                super.onBackPressed()
                return
            }
            if (supportFragmentManager.findFragmentByTag(ChangeFragment.PAYMENT_FRAGMENT.value) != null) {
                super.onBackPressed()
                return
            }
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.WELCOME_FRAGMENT.value) != null) {
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT.value) != null) {
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.SELLER_FRAGMENT.value) != null) {
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.ADMIN_FRAGMENT.value) != null) {
            if (supportFragmentManager.findFragmentByTag(ChangeFragment.SHOW_PLANT_DETAILED_FRAGMENT.value) != null) {
                super.onBackPressed()
                return
            } else {
                this.closeApplicationAlertDialog(supportFragmentManager).show()
            }
        }
    }

}