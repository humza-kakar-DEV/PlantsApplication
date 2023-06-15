package com.example.plantsservicefyp.repository.plant

import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot

interface PlantSearchRepository {
    fun getAllPlants(callback: (UiState<List<DocumentSnapshot>>) -> Unit)

    fun getApprovedPlants (callback: (UiState<List<DocumentSnapshot>>) -> Unit)

    fun getCartItems(buyerId: String, plantList: (UiState<List<DocumentSnapshot>>) -> Unit, cartItems: (UiState<List<DocumentSnapshot>>) -> Unit)

    fun searchByCategory(
        searchCategory: String,
        callback: (UiState<List<DocumentSnapshot>>) -> Unit
    )

    fun searchByName(searchName: String, callback: (UiState<DocumentSnapshot>) -> Unit)

    fun deleteCartItem(cartItem: DocumentSnapshot)

    fun deleteAllCartItems (cartItems: List<DocumentSnapshot>)
}
