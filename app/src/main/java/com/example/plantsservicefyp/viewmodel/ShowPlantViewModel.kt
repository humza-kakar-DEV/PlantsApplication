package com.example.plantsservicefyp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apitesting.model.reponse.PlantsIdentification
import com.example.plantsservicefyp.model.firebase.Cart
import com.example.plantsservicefyp.model.firebase.Favourite
import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowPlantViewModel @Inject constructor(
    private var plantRepository: PlantRepository,
    private var plantSearchRepository: PlantSearchRepository,
) : ViewModel() {

    private var observeFavouritePlantItems = MutableLiveData<UiState<List<DocumentSnapshot>>>()
    val _observeFavouritePlantItems: LiveData<UiState<List<DocumentSnapshot>>>
        get() = observeFavouritePlantItems

    private var observeFavouriteItems = MutableLiveData<UiState<List<DocumentSnapshot>>>()
    val _observeFavouriteItems: LiveData<UiState<List<DocumentSnapshot>>>
        get() = observeFavouriteItems

    private var observeFavouriteContainsPlant = MutableLiveData<DocumentSnapshot?>()
    val _observeFavouriteContainsPlant: LiveData<DocumentSnapshot?>
        get() = observeFavouriteContainsPlant

    fun addItemToCart(cart: Cart) {
        plantRepository.addItemToCart(cart)
    }

    fun addItemToFavourite(favourite: Favourite) {
        plantRepository.addItemToFavourite(favourite)
    }

    fun getFavouriteItems(buyerId: String) {
        plantSearchRepository.getFavouriteItems(buyerId, {plantItems->
            observeFavouritePlantItems.value = plantItems
        }, {favouriteItems->
            observeFavouriteItems.value = favouriteItems
        })
    }

    fun deleteFavouriteItem(favouriteItem: DocumentSnapshot) {
        plantSearchRepository.deleteFavouriteItem(favouriteItem)
    }

    fun favouriteContainsPlant(buyerId: String, plantId: String) {
        plantSearchRepository.favouriteContainsPlant(buyerId, plantId) {
            observeFavouriteContainsPlant.value = it
        }
    }

}