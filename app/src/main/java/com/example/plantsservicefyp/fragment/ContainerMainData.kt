package com.example.plantsservicefyp.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentContainer
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentContainerMainDataBinding

class ContainerMainData : Fragment() {

    private lateinit var binding: FragmentContainerMainDataBinding

    private val homeFragment: HomeFragment by lazy {
        HomeFragment(::homeFragmentCallBack)
    }

    private val seeAllFragment: SeeAllFragment by lazy {
        SeeAllFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerMainDataBinding.inflate(layoutInflater, container, false)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_top_app_bar -> {
                    val searchViewItem: MenuItem =
                        binding.topAppBar.menu.findItem(R.id.search_top_app_bar)
                    val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
                    val textView: TextView? =
                        searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
                    textView?.setTextColor(Color.parseColor("#FFFFFFFF"))
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean {
                            Toast.makeText(context, "search item", Toast.LENGTH_SHORT).show()
                            return false
                        }

                        override fun onQueryTextChange(p0: String?): Boolean {
                            Toast.makeText(context, "search item", Toast.LENGTH_SHORT).show()
                            return false
                        }
                    })
                    true
                }
                else -> false
            }
        }

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(binding.mainDataFrameLayout.id, homeFragment)
            .setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
            )
            .commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_tab -> {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(binding.mainDataFrameLayout.id, homeFragment)
                        .commit()
                }
                R.id.favourite_tab -> {
                }
                R.id.sell_tab -> {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(
                            binding.mainDataFrameLayout.id,
                            SellPlantFragment()
                        )
                        .commit()
                }
                R.id.cart_tab -> {
                }
                R.id.profile_tab -> {
                }
            }
            true
        }

        return binding.root
    }

    private fun homeFragmentCallBack() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
            )
            .replace(binding.mainDataFrameLayout.id, seeAllFragment)
            .commit()
    }
}