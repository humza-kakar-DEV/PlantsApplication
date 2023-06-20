package com.example.plantsservicefyp.fragment.buyer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentContainerMainDataBinding
import com.example.plantsservicefyp.util.*
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.ContainerMainDataViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContainerMainData : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentContainerMainDataBinding

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var homeFragment: HomeFragment

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private val containerMainDataViewModel: ContainerMainDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerMainDataBinding.inflate(layoutInflater, container, false)

        context?.log("container main data executed")

        homeFragment = HomeFragment()

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Buyer -> {
                    binding.navigationView.getHeaderView(0)
                        .findViewById<TextView>(R.id.drawerEmailTextView).text = it.user.email
                    binding.navigationView.getHeaderView(0)
                        .findViewById<TextView>(R.id.drawerUserName).text = it.user.name
                }
                else -> {
                    "user not needed"
                }
            }
        }

        actionBarDrawerToggle =
            ActionBarDrawerToggle(
                activity,
                binding.drawerLayout,
                binding.topAppBar,
                R.string.nav_close,
                R.string.nav_open
            )
        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bottomNavigationView.setSelectedItemId(R.id.home_tab)
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

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_tab -> {
                    binding.topAppBar.title = "Home"
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(binding.mainDataFrameLayout.id, homeFragment)
                        .commit()
                }
                R.id.favourite_tab -> {
                    binding.topAppBar.title = "Favourite"
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(binding.mainDataFrameLayout.id, FavouriteFragment())
                        .commit()
                }
                R.id.cart_tab -> {
                    binding.topAppBar.title = "Cart"
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

    override fun onResume() {
        super.onResume()
        context?.log("container main fragment clicked skjdflskjdflkjdsf")
        binding.bottomNavigationView.setSelectedItemId(R.id.home_tab)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_drawer_logout) {
            sharedViewModel.destroyFragment(true)
            authenticationViewModel.signOut()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .remove(homeFragment)
                .commit()
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
            sharedViewModel.changeFragment(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT)
            onDestroy()
        } else if (item.itemId == R.id.nav_drawer_about) {
            requireActivity().showAlert(R.layout.about_us_alert_dialog).show()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else if (item.itemId == R.id.nav_drawer_share) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val appPackageName = context?.packageName
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "https://drive.google.com/file/d/1D9O05s_GQ7Bewrrt2XKspjFt0vgIbgLS/view?usp=sharing"
            )
            sendIntent.setType("text/plain")
            context?.startActivity(sendIntent)
        }
        return true
    }


}