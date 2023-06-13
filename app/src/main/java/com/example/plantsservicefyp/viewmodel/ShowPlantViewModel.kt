package com.example.plantsservicefyp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.model.Cart
import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SnapshotMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowPlantViewModel @Inject constructor(
    private var plantRepository: PlantRepository,
    private var plantSearchRepository: PlantSearchRepository,
) : ViewModel() {

    fun addItemToCart(cart: Cart) {
        plantRepository.addItemToCart(cart)
    }

}