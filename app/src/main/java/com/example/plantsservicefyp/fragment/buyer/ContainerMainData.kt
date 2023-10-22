package com.example.plantsservicefyp.fragment.buyer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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

    private var fabToggleState = true

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private val containerMainDataViewModel: ContainerMainDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerMainDataBinding.inflate(layoutInflater, container, false)

        homeFragment = HomeFragment()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setSelectedItemId(R.id.home_tab)

        binding.moreTabsFab.setImageDrawable(requireContext().getDrawable(R.drawable.add_icon_32))
        binding.moreTabsFab.imageTintList = requireContext().getColorStateList(R.color.white)

        binding.fab1.setImageDrawable(requireContext().getDrawable(R.drawable.play_icon_32))
        binding.fab1.imageTintList = requireContext().getColorStateList(R.color.white)

        binding.fab2.setImageDrawable(requireContext().getDrawable(R.drawable.chat_icon_32))
        binding.fab2.imageTintList = requireContext().getColorStateList(R.color.white)

        binding.fab1.setOnClickListener {
            fabToggleState = !fabToggleState
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(binding.moreTabsFab, "rotation", 45f, 0f),
                    ObjectAnimator.ofFloat(binding.fab2, "translationX", -35f),
                    ObjectAnimator.ofFloat(binding.fab2, "translationY", 65f),
                    ObjectAnimator.ofFloat(binding.fab2, "alpha", 0f),
                    ObjectAnimator.ofFloat(binding.fab1, "translationX", 35f),
                    ObjectAnimator.ofFloat(binding.fab1, "translationY", 65f),
                    ObjectAnimator.ofFloat(binding.fab1, "alpha", 0f).apply {
                        addUpdateListener {
                            if ((it.animatedValue as Float) == 0f) {
                                binding.fab1.visibility = View.GONE
                                binding.fab2.visibility = View.GONE
                                sharedViewModel.changeFragment(ChangeFragment.VIDEO_LIST_FRAGMENT)
                            }
                        }
                    }
                )
                duration = 600L
                start()
            }
        }

        binding.fab2.setOnClickListener {
            fabToggleState = !fabToggleState
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(binding.moreTabsFab, "rotation", 45f, 0f),
                    ObjectAnimator.ofFloat(binding.fab2, "translationX", -35f),
                    ObjectAnimator.ofFloat(binding.fab2, "translationY", 65f),
                    ObjectAnimator.ofFloat(binding.fab2, "alpha", 0f),
                    ObjectAnimator.ofFloat(binding.fab1, "translationX", 35f),
                    ObjectAnimator.ofFloat(binding.fab1, "translationY", 65f),
                    ObjectAnimator.ofFloat(binding.fab1, "alpha", 0f).apply {
                        addUpdateListener {
                            if ((it.animatedValue as Float) == 0f) {
                                binding.fab1.visibility = View.GONE
                                binding.fab2.visibility = View.GONE
                                sharedViewModel.changeFragment(ChangeFragment.CHAT_FRAGMENT)
                            }
                        }
                    }
                )
                duration = 600L
                start()
            }
        }

        binding.moreTabsFab.setOnClickListener {
            if (fabToggleState) {
                binding.fab1.visibility = View.VISIBLE
                binding.fab2.visibility = View.VISIBLE
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.moreTabsFab, "rotation", 0f, 45f),
                        ObjectAnimator.ofFloat(binding.fab2, "translationX", 0f),
                        ObjectAnimator.ofFloat(binding.fab2, "translationY", 0f),
                        ObjectAnimator.ofFloat(binding.fab2, "alpha", 1f),
                        ObjectAnimator.ofFloat(binding.fab1, "translationX", 0f),
                        ObjectAnimator.ofFloat(binding.fab1, "translationY", 0f),
                        ObjectAnimator.ofFloat(binding.fab1, "alpha", 1f)
                    )
                    duration = 600L
                    start()
                }
            } else {
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.moreTabsFab, "rotation", 45f, 0f),
                        ObjectAnimator.ofFloat(binding.fab2, "translationX", -35f),
                        ObjectAnimator.ofFloat(binding.fab2, "translationY", 65f),
                        ObjectAnimator.ofFloat(binding.fab2, "alpha", 0f),
                        ObjectAnimator.ofFloat(binding.fab1, "translationX", 35f),
                        ObjectAnimator.ofFloat(binding.fab1, "translationY", 65f),
                        ObjectAnimator.ofFloat(binding.fab1, "alpha", 0f).apply {
                            addUpdateListener {
                                if ((it.animatedValue as Float) == 0f) {
                                    binding.fab1.visibility = View.GONE
                                    binding.fab2.visibility = View.GONE
                                }
                            }
                        }
                    )
                    duration = 600L
                    start()
                }
            }
            fabToggleState = !fabToggleState
        }

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

                R.id.cart_tab -> {
                    binding.topAppBar.title = "Reminder"
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            binding.mainDataFrameLayout.id,
                            CartFragment()
                        )
                        .commit()
                }

                R.id.reminder_tab -> {
                    binding.topAppBar.title = "Reminder"
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            binding.mainDataFrameLayout.id,
                            SetReminderFragment()
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