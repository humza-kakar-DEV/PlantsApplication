package com.example.plantsservicefyp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val plantSearchRepository: PlantSearchRepository,
    private @ApplicationContext var context: Context,
) : ViewModel() {

    private var observePlantsFromCart = MutableLiveData<UiState<List<DocumentSnapshot>>>()
    val _observePlantsFromCart: LiveData<UiState<List<DocumentSnapshot>>>
        get() = observePlantsFromCart

    private var observeCartItems = MutableLiveData<UiState<List<DocumentSnapshot>>>()
    val _observeCartItems: LiveData<UiState<List<DocumentSnapshot>>>
        get() = observeCartItems

    fun getCartItems(buyerId: String) {
        context?.log("get cart items called")
        plantSearchRepository.getCartItems(buyerId,
            {plantItems->
                observePlantsFromCart.value = plantItems
            }, {cartItems->
                observeCartItems.value = cartItems
            })
    }

    fun deleteCartItems (buyerId: String, cartItem: DocumentSnapshot) {
//        delete cart item also update UI about latest data in the cart
        plantSearchRepository.deleteCartItem(cartItem)
        getCartItems(buyerId)
    }

    fun deleteAllCartItems (cartItems: List<DocumentSnapshot>) {
        plantSearchRepository.deleteAllCartItems(cartItems)
    }

}