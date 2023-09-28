package com.example.plantsservicefyp.activity

import com.example.plantsservicefyp.R
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.plantsservicefyp.fragment.buyer.VideoPlayerFragment
import com.example.plantsservicefyp.databinding.ActivityMainBinding
import com.example.plantsservicefyp.fragment.admin.ApprovalFragment
import com.example.plantsservicefyp.fragment.admin.ShowDetailedPlant
import com.example.plantsservicefyp.fragment.auth.ContainerAuthenticationFragment
import com.example.plantsservicefyp.fragment.auth.WelcomeFragment
import com.example.plantsservicefyp.fragment.buyer.ChatFragment
import com.example.plantsservicefyp.fragment.buyer.ContainerMainData
import com.example.plantsservicefyp.fragment.buyer.PaymentFragment
import com.example.plantsservicefyp.fragment.buyer.ShowPlantFragment
import com.example.plantsservicefyp.fragment.buyer.VideoListFragment
import com.example.plantsservicefyp.fragment.seller.SellPlantFragment
import com.example.plantsservicefyp.util.clearAllBackStackExcept
import com.example.plantsservicefyp.util.closeApplicationAlertDialog
import com.example.plantsservicefyp.util.constant.ChangeFragment
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

//        forcing light theme
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
                    supportFragmentManager.clearAllBackStackExcept(
                        ChangeFragment.values().toMutableList(),
                        ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT
                    )
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
                        .addToBackStack(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT.value)
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
                    supportFragmentManager.clearAllBackStackExcept(
                        ChangeFragment.values().toMutableList(), ChangeFragment.ADMIN_FRAGMENT
                    )
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
                        .addToBackStack(ChangeFragment.ADMIN_FRAGMENT.value)
                        .commit()
                }

                ChangeFragment.SELLER_FRAGMENT -> {
                    supportFragmentManager.clearAllBackStackExcept(
                        ChangeFragment.values().toMutableList(), ChangeFragment.SELLER_FRAGMENT
                    )
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
                        .addToBackStack(ChangeFragment.SELLER_FRAGMENT.value)
                        .commit()
                }

                ChangeFragment.BUYER_FRAGMENT -> {
                    supportFragmentManager.clearAllBackStackExcept(
                        ChangeFragment.values().toMutableList(), ChangeFragment.BUYER_FRAGMENT
                    )
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
                        .addToBackStack(ChangeFragment.BUYER_FRAGMENT.value)
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

                ChangeFragment.WELCOME_FRAGMENT -> {}
                ChangeFragment.VIDEO_LIST_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.slide_out,
                            R.anim.slide_in,
                            R.anim.slide_out,
                        )
                        .replace(
                            binding.activityMainFrameLayout.id,
                            VideoListFragment(),
                            ChangeFragment.VIDEO_LIST_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }

                ChangeFragment.VIDEO_PLAYER_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.slide_out,
                            R.anim.slide_in,
                            R.anim.slide_out,
                        )
                        .replace(
                            binding.activityMainFrameLayout.id,
                            VideoPlayerFragment(),
                            ChangeFragment.VIDEO_PLAYER_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }

                ChangeFragment.CHAT_FRAGMENT -> {
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.slide_out,
                            R.anim.slide_in,
                            R.anim.slide_out,
                        )
                        .replace(
                            binding.activityMainFrameLayout.id,
                            ChatFragment(),
                            ChangeFragment.CHAT_FRAGMENT.value
                        )
                        .addToBackStack(null)
                        .commit()
                }
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
            if (supportFragmentManager.findFragmentByTag(ChangeFragment.VIDEO_LIST_FRAGMENT.value) != null) {
                super.onBackPressed()
                return
            }
            if (supportFragmentManager.findFragmentByTag(ChangeFragment.VIDEO_PLAYER_FRAGMENT.value) != null) {
                super.onBackPressed()
                return
            }
            if (supportFragmentManager.findFragmentByTag(ChangeFragment.CHAT_FRAGMENT.value) != null) {
                super.onBackPressed()
                return
            }
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.WELCOME_FRAGMENT.value) != null) {
            this.toast("welcome fragment founded")
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT.value) != null) {
            this.toast("authentication fragment founded")
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.SELLER_FRAGMENT.value) != null) {
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        } else if (supportFragmentManager.findFragmentByTag(ChangeFragment.ADMIN_FRAGMENT.value) != null) {
            if (supportFragmentManager.findFragmentByTag(ChangeFragment.SHOW_PLANT_DETAILED_FRAGMENT.value) != null) {
                super.onBackPressed()
                return@onBackPressed
            }
            this.closeApplicationAlertDialog(supportFragmentManager).show()
        }
    }

}