package com.example.plantsservicefyp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private var plantSearchRepository: PlantSearchRepository,
): ViewModel() {

    private var observeFavouritePlantItems = MutableLiveData<UiState<List<DocumentSnapshot>>>()
    val _observeFavouritePlantItems: LiveData<UiState<List<DocumentSnapshot>>>
        get() = observeFavouritePlantItems

    private var observeFavouriteItems = MutableLiveData<UiState<List<DocumentSnapshot>>>()
    val _observeFavouriteItems: LiveData<UiState<List<DocumentSnapshot>>>
        get() = observeFavouriteItems

    fun getFavouriteItems (buyerId: String) {
        plantSearchRepository.getFavouriteItems(buyerId, {plantItems->
            observeFavouritePlantItems.value = plantItems
        }, {favouriteItems->
            observeFavouriteItems.value = favouriteItems
        })
    }

}