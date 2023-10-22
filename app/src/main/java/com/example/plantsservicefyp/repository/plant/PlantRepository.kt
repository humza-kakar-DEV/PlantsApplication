package com.example.plantsservicefyp.repository.plant

import android.net.Uri
import com.example.apitesting.model.reponse.PlantsIdentification
import com.example.plantsservicefyp.model.firebase.Cart
import com.example.plantsservicefyp.model.firebase.Favourite
import com.example.plantsservicefyp.model.firebase.Message
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.UiState
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    fun addPlant(plant: Plant, imageUri: Uri, callback: (UiState<Plant>?) -> Unit)

    fun addItemToCart(cart: Cart)

    suspend fun identifyPlant(imageList: List<Uri>): PlantsIdentification

    fun addItemToFavourite(favourite: Favourite)

    fun writeMessage(message: Message): Flow<Unit>

    fun getAllMessages(): Flow<List<Message>>
}