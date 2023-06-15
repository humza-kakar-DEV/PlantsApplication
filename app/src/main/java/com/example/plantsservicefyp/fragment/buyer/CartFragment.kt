package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.adapter.CartRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentCartBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.CartViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private lateinit var cartRecyclerViewAdapter: CartRecyclerViewAdapter

    private val cartViewModel: CartViewModel by viewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private var cartItems = mutableListOf<DocumentSnapshot>()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var totalPrice: Int = 0

    private var buyerId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)

        cartRecyclerViewAdapter = CartRecyclerViewAdapter(requireContext()) { adapterPosition ->
//            delete document
            cartViewModel.deleteCartItems(
                buyerId = buyerId,
                cartItem = cartItems.get(adapterPosition)
            )
        }

        cartViewModel._observeCartItems.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                }
                is UiState.Success -> {
                    if (it.data?.isNotEmpty()!!) {
                        cartItems.clear()
                        cartItems.addAll(it.data)
                    } else {
                        cartItems.clear()
                        context?.log("cart items: emptyList()")
                    }
                }
                is UiState.Exception -> {

                }
            }
        }

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Buyer -> {
                    buyerId = it.user.userId.toString()
                    cartViewModel.getCartItems(buyerId = it.user.userId.toString())
                }
                else -> {
                    "irrelavant users"
                }
            }
        }

        cartViewModel._observePlantsFromCart.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                }
                is UiState.Success -> {
//                    cartRecyclerViewAdapter
                    if (it.data?.isNotEmpty()!!) {
                        cartRecyclerViewAdapter.updateAdapterWithDocuments(it.data)
//                        implementing total price logic
                        totalPrice = 0
                        it.data?.forEach {
                            it.toObject(Plant::class.java).apply {
                                totalPrice += Integer.valueOf(this?.price)
                            }
                        }
                        binding.cartFragmentPriceDataTextView.text = "Rs. ${totalPrice.toString()}"
                    } else {
//                        empty list show empty cart icon
                        cartRecyclerViewAdapter.updateAdapterWithDocuments(emptyList())
                        binding.cartFragmentPriceDataTextView.text = "Rs. 0"
                        context?.log("plant list: emptyList()")
                    }
                }
                is UiState.Exception -> {
                    requireContext().log("cart fragment: exception")
                }
            }
        }

        binding.cartFragmentProceedToCheckButton.setOnClickListener {
            if (totalPrice != 0) {
                context?.toast("check out!!!")
                sharedViewModel.changeFragment(ChangeFragment.PAYMENT_FRAGMENT)
                sharedViewModel.setTotalPrice(totalPrice.toString())
                sharedViewModel.setBoughtItems(cartItems)
                cartViewModel.deleteAllCartItems(cartItems = cartItems)
            } else {
                context?.toast("empty cart :(")
            }
        }

        binding.cartFragmentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.cartFragmentRecyclerView.adapter = cartRecyclerViewAdapter

        return binding.root
    }
}