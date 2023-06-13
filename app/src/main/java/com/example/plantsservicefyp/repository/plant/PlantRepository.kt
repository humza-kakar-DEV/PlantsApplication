package com.example.plantsservicefyp.repository

import android.net.Uri
import com.example.plantsservicefyp.model.Cart
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.UiState

interface PlantRepository {
    fun addPlant(plant: Plant, imageUri: Uri, callback: (UiState<Plant>?) -> Unit)

    fun addItemToCart(cart: Cart)
}