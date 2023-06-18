package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.CartRecyclerViewAdapter
import com.example.plantsservicefyp.adapter.FavouriteItemRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentFavouriteBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.FavouriteViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    private val favouriteViewModel: FavouriteViewModel by viewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private lateinit var favouriteItemRecyclerViewAdapter: FavouriteItemRecyclerViewAdapter

    private var favouriteItems = mutableListOf<DocumentSnapshot>()

    private var plantsFromFavouriteItems = mutableListOf<DocumentSnapshot>()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var totalPrice: Int = 0

    private var buyerId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(layoutInflater, container, false)

        favouriteItemRecyclerViewAdapter = FavouriteItemRecyclerViewAdapter(requireContext()) { adapterPosition ->
//            delete document
//            cartViewModel.deleteCartItems(
//                buyerId = buyerId,
//                cartItem = cartItems.get(adapterPosition)
//            )
            sharedViewModel.setSelectedPlant(plantsFromFavouriteItems.get(adapterPosition))
            sharedViewModel.changeFragment(ChangeFragment.SHOW_PLANT_FRAGMENT)
        }

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Buyer -> {
                    it.user.apply {
                        buyerId = userId
                        favouriteViewModel.getFavouriteItems(userId)
                    }
                }
                else -> {}
            }
        }

        favouriteViewModel._observeFavouriteItems.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {}
                is UiState.Success -> {
                    it.data?.let {
                        if (it.isNotEmpty()) {
                            binding.favouriteFragmentEmptyListLayout.visibility = View.GONE
                            binding.favouriteFragmentRecyclerView.visibility = View.VISIBLE
                            favouriteItems.clear()
                            favouriteItems.addAll(it)
                        } else {
                            favouriteItems.clear()
                            binding.favouriteFragmentEmptyListLayout.visibility = View.VISIBLE
                            binding.favouriteFragmentRecyclerView.visibility = View.GONE
                        }
                    }
                }
                is UiState.Exception -> {}
            }
        }

        favouriteViewModel._observeFavouritePlantItems.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                }
                is UiState.Success -> {
//                    cartRecyclerViewAdapter
                    if (it.data?.isNotEmpty()!!) {
                        context?.log("fav items from fragment size: ${it.data.size}")
                        binding.itemFoundTextView.text = "${it.data?.size} founds"
                        plantsFromFavouriteItems.clear()
                        plantsFromFavouriteItems.addAll(it.data)
                        favouriteItemRecyclerViewAdapter.updateAdapterWithDocuments(it.data)
//                        implementing total price logic
                        totalPrice = 0
                        it.data?.forEach {
                            it.toObject(Plant::class.java).apply {
                                totalPrice += Integer.valueOf(this?.price)
                            }
                        }

//                        context?.log(it.data?.createReceipt(totalPrice.toString()).toString())

                    } else {
//                        empty list show empty cart icon
                        favouriteItemRecyclerViewAdapter.updateAdapterWithDocuments(emptyList())
                    }

                }
                is UiState.Exception -> {
                    requireContext().log("cart fragment: exception")
                }
            }
        }

        binding.favouriteFragmentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.favouriteFragmentRecyclerView.adapter = favouriteItemRecyclerViewAdapter

        return binding.root
    }
}