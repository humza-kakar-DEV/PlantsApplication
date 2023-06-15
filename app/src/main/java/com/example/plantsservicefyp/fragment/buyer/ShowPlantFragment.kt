package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentShowPlantBinding
import com.example.plantsservicefyp.model.firebase.Cart
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.example.plantsservicefyp.viewmodel.ShowPlantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowPlantFragment() : Fragment() {

    private lateinit var binding: FragmentShowPlantBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val showPlantViewModel: ShowPlantViewModel by viewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private var cart = Cart()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowPlantBinding.inflate(layoutInflater, container, false)

        sharedViewModel._observeSelectedPlant.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.showPlantNotFoundLayout.visibility = View.GONE
                binding.showPlantMainDataConstraintLayout.visibility = View.VISIBLE
                it.toObject(Plant::class.java).apply {
                    binding.showPlantNameTextView.text = this?.name
                    binding.showPlantDescriptionDataTextView.text = this?.description
                    binding.showPlantPriceTextView.text = "Rs. ${this?.price}"
                    Glide
                        .with(requireContext())
                        .load(this?.imageDownloadUrl)
                        .centerCrop()
                        .placeholder(R.drawable.baseline_file_download_24)
                        .into(binding.showPlantImageView)
                }
                cart.apply {
                    plantId = it.id.toString()
                }
            } else {
                binding.showPlantNotFoundLayout.visibility = View.VISIBLE
                binding.showPlantMainDataConstraintLayout.visibility = View.GONE
            }
        }

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Admin -> context?.log(it.user.userId.toString())
                is CurrentUserType.Buyer -> {
                    context?.log("buyer id: ${it.user.userId.toString()}")
                    cart.buyerId = it.user.userId.toString()
                }
                is CurrentUserType.Seller -> context?.log(it.user.userId.toString())
                is CurrentUserType.Exception -> context?.log("exception: ${it.error.toString()}")
                CurrentUserType.Loading -> context?.log("show plant fragment: LOADING")
            }
        }

        binding.addToCartButton.setOnClickListener {
            showPlantViewModel.addItemToCart(cart)
        }

        return binding.root
    }
}