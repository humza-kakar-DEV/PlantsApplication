package com.example.plantsservicefyp.repository.plant

import com.example.plantsservicefyp.model.firebase.Favourite
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot
import org.w3c.dom.Document

interface PlantSearchRepository {
    fun getAllPlants(callback: (UiState<List<DocumentSnapshot>>) -> Unit)

    fun getApprovedPlants(callback: (UiState<List<DocumentSnapshot>>) -> Unit)

    fun getCartItems(
        buyerId: String,
        plantList: (UiState<List<DocumentSnapshot>>) -> Unit,
        cartItems: (UiState<List<DocumentSnapshot>>) -> Unit
    )

    fun deleteCartItem(cartItem: DocumentSnapshot)

    fun deleteAllCartItems(cartItems: List<DocumentSnapshot>)


    fun getFavouriteItems(
        buyerId: String,
        plantListCallBack: (UiState<List<DocumentSnapshot>>) -> Unit,
        favouriteItems: (UiState<List<DocumentSnapshot>>) -> Unit
    )

    fun deleteFavouriteItem(favouriteItem: DocumentSnapshot)

    fun favouriteContainsPlant(
        buyerId: String,
        plantId: String,
        callback: (DocumentSnapshot?) -> Unit
    )

    fun searchByCategory(
        searchCategory: String,
        callback: (UiState<List<DocumentSnapshot>>) -> Unit
    )

    fun searchByName(searchName: String, callback: (UiState<DocumentSnapshot>) -> Unit)

    fun editPlantState(plant: Plant, callback: (UiState<String>) -> Unit)

    fun updatePlantsSold(plantsFromCart: List<DocumentSnapshot>)
}
