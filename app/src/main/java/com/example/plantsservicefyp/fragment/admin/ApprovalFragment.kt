package com.example.plantsservicefyp.fragment.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.ApprovalRecyclerViewAdapter
import com.example.plantsservicefyp.adapter.CartRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentApprovalBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.*
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.showAlert
import com.example.plantsservicefyp.viewmodel.ApprovalViewModel
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApprovalFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentApprovalBinding

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private val approvalViewModel: ApprovalViewModel by viewModels()

    private lateinit var approvalRecyclerViewAdapter: ApprovalRecyclerViewAdapter

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApprovalBinding.inflate(layoutInflater, container, false);

        binding.chatFab.setImageDrawable(requireContext().getDrawable(R.drawable.chat_icon_32))
        binding.chatFab.imageTintList = requireContext().getColorStateList(R.color.white)

        binding.chatFab.setOnClickListener {
            sharedViewModel.changeFragment(ChangeFragment.CHAT_FRAGMENT)
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

        approvalRecyclerViewAdapter =
            ApprovalRecyclerViewAdapter(requireContext(), { statePlant ->
                approvalViewModel.editPlantState(statePlant)
            }, { detailedPlant ->
                sharedViewModel.setSelectedPlant(detailedPlant)
                detailedPlant.toObject(Plant::class.java)?.sellerId?.let {
                    authenticationViewModel.getUserWithId(
                        it
                    )
                }
                authenticationViewModel._observeUser.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Success -> {
                            sharedViewModel.setObserveSellerUser(it.data!!)
                            sharedViewModel.changeFragment(ChangeFragment.SHOW_PLANT_DETAILED_FRAGMENT)
                        }
                        else -> {}
                    }
                }
            })

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Admin -> {
                    binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerEmailTextView).text = it.user.email
                    binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerUserName).text = it.user.name
                }
                else -> {
                    "irrelevant id's"
                }
            }
        }

        approvalViewModel._observePlantState.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {

                }
                is UiState.Success -> {
                    approvalViewModel.getAllPlants()
                }
                is UiState.Exception -> {

                }
            }
        }

        approvalViewModel._observeAllPlants.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {

                }
                is UiState.Success -> {
                    it.data?.let {
                        if (it.isNotEmpty()) {
                            binding.itemFoundTextView.text = "${it.size} founds"
                            approvalRecyclerViewAdapter.updateAdapterWithDocuments(it)
                        } else {
                            context?.log("plant list is empty")
                        }
                    }
                }
                is UiState.Exception -> {
                }
            }
        }

        binding.approvalFragmentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.approvalFragmentRecyclerView.adapter = approvalRecyclerViewAdapter

        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_drawer_logout) {
            authenticationViewModel.signOut()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            sharedViewModel.changeFragment(ChangeFragment.CONTAINER_AUTHENTICATION_FRAGMENT)
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