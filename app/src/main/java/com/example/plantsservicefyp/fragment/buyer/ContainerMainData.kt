package com.example.plantsservicefyp.fragment.buyer

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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.activity.MainActivity
import com.example.plantsservicefyp.databinding.FragmentContainerMainDataBinding
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.ContainerMainDataViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContainerMainData : Fragment() {

    private lateinit var binding: FragmentContainerMainDataBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val containerMainDataViewModel: ContainerMainDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerMainDataBinding.inflate(layoutInflater, container, false)

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(binding.mainDataFrameLayout.id, HomeFragment())
            .commit()

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
                            sharedViewModel.searchByName(p0.toString())
                            sharedViewModel.changeFragment(ChangeFragment.SHOW_PLANT_FRAGMENT)
                            menuItem.collapseActionView()
                            return false
                        }

                        override fun onQueryTextChange(p0: String?): Boolean {
                            return false
                        }
                    })
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigationView.setSelectedItemId(R.id.home_tab)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_tab -> {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(binding.mainDataFrameLayout.id, HomeFragment())
                        .commit()
                }
                R.id.favourite_tab -> {
                }
                R.id.cart_tab -> {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            binding.mainDataFrameLayout.id,
                            CartFragment()
                        )
                        .commit()
                }
            }
            true
        }

        return binding.root
    }

//    override fun onStart() {
//        super.onStart()
//
//        requireActivity()
//            .supportFragmentManager
//            .beginTransaction()
//            .replace(binding.mainDataFrameLayout.id, HomeFragment())
//            .commit()
//    }

}