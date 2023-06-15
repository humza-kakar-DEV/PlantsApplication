package com.example.plantsservicefyp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.model.firebase.Cart
import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
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