package com.example.plantsservicefyp.fragment.buyer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.plantsservicefyp.databinding.FragmentSellPlantBinding
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.viewmodel.SellPlantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellPlantFragment : Fragment() {

    private lateinit var binding: FragmentSellPlantBinding
    private val sellPlantViewModel: SellPlantViewModel by viewModels()

    private lateinit var plantNameInput: String
    private lateinit var plantDescriptionInput: String
    private lateinit var plantPriceInput: String
    private lateinit var plantLocationInput: String
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellPlantBinding.inflate(layoutInflater, container, false)

        sellPlantViewModel._observePlantAdd.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    Log.d("hm123", "plant -> Loading")
                    binding.createLoadingButton.start()
                }
                is UiState.Success -> {
                    Log.d("hm123", "plant -> success: ${it.data}")
                    binding.createLoadingButton.complete(true)
                }
                is UiState.Error -> {
                    Log.d("hm123", "plant -> error: ${it.exception}")
                }
            }
        }

        binding.createLoadingButton.setOnClickListener returnOnClick@{
            if (!validatePlantName() or !validatePlantDescription() or !validatePlantPrice() or !validatePlantLocation())
                return@returnOnClick
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Select plant image", Toast.LENGTH_SHORT).show()
                return@returnOnClick
            }
            Plant(
                name = plantNameInput,
                description = plantDescriptionInput,
                price = plantPriceInput,
                location = plantLocationInput,
                userId = null,
                imageDownloadUrl = null,
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

        binding.imageEidButton.setOnClickListener {
            startForResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
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

}