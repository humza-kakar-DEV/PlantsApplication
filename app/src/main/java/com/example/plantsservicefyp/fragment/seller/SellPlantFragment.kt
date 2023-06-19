package com.example.plantsservicefyp.fragment.seller

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentSellPlantBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.*
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SellPlantViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.request.*

@AndroidEntryPoint
class SellPlantFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentSellPlantBinding
    private val sellPlantViewModel: SellPlantViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var plantNameInput: String
    private lateinit var plantDescriptionInput: String
    private lateinit var plantPriceInput: String
    private lateinit var plantLocationInput: String
    private var imageUri: Uri? = null
    private var plantCategory: String? = null
    private var base64Encode: String = ""
    private lateinit var sellerId: String
    private lateinit var createAlertDialog: AlertDialog
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellPlantBinding.inflate(layoutInflater, container, false)


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

        activity?.aiLoadingAlertDialog()?.let {
            createAlertDialog = it
        }

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Seller -> {
                    sellerId = it.user.userId.toString()
                    binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerEmailTextView).text = it.user.email
                    binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerUserName).text = it.user.name
                }
                else -> {
                    "irrelevant id's"
                }
            }
        }

        sellPlantViewModel._observePlantAdd.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    requireContext().log("sell plant: Loading")
                    binding.createLoadingButton.start()
                }
                is UiState.Success -> {
                    requireContext().log(it.data.toString())
                    binding.createLoadingButton.complete(true)
                }
                is UiState.Exception -> {
                    requireContext().log("sell plant: ${it.message.toString()}")
                }
            }
        }

        binding.createLoadingButton.setOnClickListener returnOnClick@{
            if (!validatePlantName() or !validatePlantDescription() or !validatePlantPrice() or !validatePlantLocation())
                return@returnOnClick
            if (imageUri == null) {
                requireContext().toast("Select plant image")
                return@returnOnClick
            }
            if (plantCategory == null) {
                requireContext().toast("Select plant category")
                return@returnOnClick
            }
            Plant(
                name = plantNameInput,
                description = plantDescriptionInput,
                price = plantPriceInput,
                location = plantLocationInput,
                sellerId = sellerId,
                imageDownloadUrl = null,
                plantCategory = plantCategory!!,
                plantState = false,
                sold = 0,
                rating = 0.0f
            ).apply {
                sellPlantViewModel.addPlant(plant = this, imageUri = imageUri!!)
            }
        }

//        image gallery intent code
        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                binding.roundImageView.setImageURI(imageUri)
            }
        }

        binding.imageEidButton2.setOnClickListener {
            startForResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }

        binding.sellAllPlantSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            plantCategory = newItem
        }

        binding.plantVerificationButton.setOnClickListener {
            if (imageUri == null) {
                context?.toast("select image first!")
            } else {
                sellPlantViewModel.indentifyPlant(mutableListOf(imageUri!!))
            }
        }

        sellPlantViewModel._observePlantIndentification.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    context?.log("api loading")
                    createAlertDialog.show()
                }
                is UiState.Success -> {
                    context?.log("api test data: ${it.data!!}")
                    it.data?.suggestions?.map {
                        it.plant_details
                    }!!.first().apply {
                        binding.textInputName.editText?.setText(this.scientific_name)
                        binding.textInputDescription.editText?.setText(this.wiki_description.value)
                    }
                    Thread(Runnable {
                        Handler(
                            Looper.getMainLooper()
                        ).postDelayed(
                            Runnable {
                                     createAlertDialog.cancel()
                        }, 3500)
                    }).start()
                }
                is UiState.Exception -> {
                    context?.log("api data: ${it.message}")
                    Thread(Runnable {
                        Handler(
                            Looper.getMainLooper()
                        ).postDelayed(
                            Runnable {
                                createAlertDialog.cancel()
                            }, 3500)
                    }).start()
                }
            }
        }

        return binding.root
    }


    private fun validatePlantName(): Boolean {
        plantNameInput = binding.textInputName.getEditText()?.getText().toString().trim()
        return if (plantNameInput.isEmpty()) {
            binding.textInputName.setError("Field can't be empty")
            false
        } else {
            binding.textInputName.setError(null)
            true
        }
    }

    private fun validatePlantDescription(): Boolean {
        plantDescriptionInput =
            binding.textInputDescription.getEditText()?.getText().toString().trim()
        return if (plantDescriptionInput.isEmpty()) {
            binding.textInputDescription.setError("Field can't be empty")
            false
        } else {
            binding.textInputDescription.setError(null)
            true
        }
    }

    private fun validatePlantPrice(): Boolean {
        plantPriceInput = binding.textInputPrice.getEditText()?.getText().toString().trim()
        return if (plantPriceInput.isEmpty()) {
            binding.textInputPrice.setError("Field can't be empty")
            false
        } else {
            binding.textInputPrice.setError(null)
            true
        }
    }

    private fun validatePlantLocation(): Boolean {
        plantLocationInput = binding.textInputPrice.getEditText()?.getText().toString().trim()
        return if (plantLocationInput.isEmpty()) {
            binding.textInputLocation.setError("Field can't be empty")
            false
        } else {
            binding.textInputLocation.setError(null)
            true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_drawer_logout) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            authenticationViewModel.signOut()
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