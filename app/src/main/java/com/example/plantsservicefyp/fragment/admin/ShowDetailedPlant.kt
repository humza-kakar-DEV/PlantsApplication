package com.example.plantsservicefyp.fragment.admin

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentShowDetailedPlantBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.model.firebase.User
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailedPlant : Fragment() {

    private lateinit var binding: FragmentShowDetailedPlantBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowDetailedPlantBinding.inflate(layoutInflater, container, false)

        sharedViewModel._observeSelectedPlant.observe(viewLifecycleOwner) {
            it?.let {
                it.toObject(Plant::class.java)?.apply {
                    Glide
                        .with(requireContext())
                        .load(Uri.parse(imageDownloadUrl))
                        .centerCrop()
                        .placeholder(R.drawable.baseline_file_download_24)
                        .into(binding.showDetailedPlantImageView)
                    binding.showDetailedPlantNameTextView.text = name
                    binding.showDetailedPlantDescriptionDataTextView.text = description
                    binding.detailedTextView2.text = plantId
                    binding.detailedTextView4.text = plantCategory
                }
            }
        }

        sharedViewModel._observeSellerUser.observe(viewLifecycleOwner) {
            it.toObject(User::class.java)?.apply {
                binding.detailedTextView7.text = email
            }
        }

        return binding.root
    }
}