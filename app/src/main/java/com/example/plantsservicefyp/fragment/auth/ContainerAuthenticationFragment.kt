package com.example.plantsservicefyp.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentContainerAuthenticationBinding

class ContainerAuthenticationFragment : Fragment() {

    private lateinit var binding: FragmentContainerAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerAuthenticationBinding.inflate(layoutInflater, container, false)

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(binding.containerFrameLayout.id, SignInFragment())
            .setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
            )
            .commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.loginMenu -> requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(
                        binding.containerFrameLayout.id,
                        SignInFragment()
                    )
                    .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                    )
                    .commit()
                R.id.registerMenu -> requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(
                        binding.containerFrameLayout.id,
                        SignUpFragment()
                    )
                    .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                    )
                    .commit()
            }
            true
        }

        return binding.root
    }
}