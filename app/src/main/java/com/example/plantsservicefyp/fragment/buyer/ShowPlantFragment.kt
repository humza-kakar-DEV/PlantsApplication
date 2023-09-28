package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentShowPlantBinding
import com.example.plantsservicefyp.model.firebase.Cart
import com.example.plantsservicefyp.model.firebase.Favourite
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.model.firebase.User
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.example.plantsservicefyp.viewmodel.ShowPlantViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShowPlantFragment : Fragment() {

    private lateinit var binding: FragmentShowPlantBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val showPlantViewModel: ShowPlantViewModel by viewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private var cart = Cart()

    private var buyerId: String = ""

    private var plantId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowPlantBinding.inflate(layoutInflater, container, false)

        sharedViewModel._observeSelectedPlant.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.showPlantNotFoundLayout.visibility = View.GONE
                binding.showPlantMainDataConstraintLayout.visibility = View.VISIBLE
                it.toObject(Plant::class.java)?.apply {
                    this@ShowPlantFragment.plantId = plantId
                    binding.showPlantNameTextView.text = name
                    binding.showPlantDescriptionDataTextView.text = description
                    binding.showPlantPriceTextView.text = "Rs. ${price}"
                    binding.detailedTextView4.text = plantCategory
                    authenticationViewModel.getUserWithId(sellerId)
                    if (sold!! >= 10) {
                        binding.detailedTextView1.text = "sold 🔥"
                    }
                    binding.detailedTextView2.text = sold.toString()
                    Glide
                        .with(requireContext())
                        .load(this.imageDownloadUrl)
                        .centerCrop()
                        .placeholder(R.drawable.baseline_file_download_24)
                        .into(binding.showPlantImageView)

                    authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
                        when (it) {
                            is CurrentUserType.Buyer -> {
                                it.user.apply {
                                    cart.buyerId = userId
                                    buyerId = userId
                                    showPlantViewModel.favouriteContainsPlant(buyerId, plantId)
                                }
                            }
                            else -> {}
                        }
                    }

                }
                cart.apply {
                    plantId = it.id.toString()
                }
                binding.addToCartButton.setOnClickListener {
                    showPlantViewModel.addItemToCart(cart)
                    binding.addToCartButton.start()
                    Thread(Runnable {
                        Handler(
                            Looper.getMainLooper()
                        ).postDelayed(
                            Runnable {
                                binding.addToCartButton.complete(true)
                            }, 1500)
                    }).start()
                }
            } else {
                binding.showPlantNotFoundLayout.visibility = View.VISIBLE
                binding.showPlantMainDataConstraintLayout.visibility = View.GONE
            }
        }

        authenticationViewModel._observeUser.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {}
                is UiState.Success -> {
                    it.data?.toObject(User::class.java)?.apply {
                        binding.detailedTextView7.text = email
                    }
                }
                is UiState.Exception -> {}
            }
        }

        showPlantViewModel._observeFavouriteContainsPlant.observe(viewLifecycleOwner) { favouriteItem ->
            binding.favouriteCheckBox.isChecked = favouriteItem != null

            binding.favouriteCheckBox.setOnClickListener {
                ViewCompat.animate(binding.favouriteCheckBox).scaleX(1.3f).scaleY(1.3f)
                    .setDuration(100).setListener(object : ViewPropertyAnimatorListener {
                        override fun onAnimationStart(view: View) {
                        }

                        override fun onAnimationEnd(view: View) {
                            ViewCompat.animate(binding.favouriteCheckBox).scaleX(1f).scaleY(1f)
                                .setDuration(100)
                        }

                        override fun onAnimationCancel(view: View) {
                        }

                    })
                if (binding.favouriteCheckBox.isChecked) {
                    showPlantViewModel.favouriteContainsPlant(buyerId, plantId)
                    showPlantViewModel.addItemToFavourite(
                        favourite = Favourite(
                            buyerId = buyerId,
                            plantId = plantId,
                        )
                    )
                } else {
                    ViewCompat.animate(binding.favouriteCheckBox).alpha(1f).scaleX(1f).scaleY(1f)
                        .setDuration(600).interpolator = AccelerateInterpolator()
                    showPlantViewModel.favouriteContainsPlant(buyerId, plantId)
                    favouriteItem?.let {
                        showPlantViewModel.deleteFavouriteItem(it)
                    }
                }
            }
        }


        return binding.root
    }
}