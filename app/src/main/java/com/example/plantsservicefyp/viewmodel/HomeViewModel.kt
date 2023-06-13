package com.example.plantsservicefyp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantSearchRepository: PlantSearchRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private var _observeSearchPlantList = MutableLiveData<UiState<List<DocumentSnapshot>>>()

    val observeSearchPlantList: LiveData<UiState<List<DocumentSnapshot>>>
        get() = _observeSearchPlantList

    fun getAllPlants() = plantSearchRepository.getAllPlants {
        _observeSearchPlantList.value = it
    }

    fun searchByCategory(searchCategory: String) =
        plantSearchRepository.searchByCategory(searchCategory) {
            _observeSearchPlantList.value = it
        }

}