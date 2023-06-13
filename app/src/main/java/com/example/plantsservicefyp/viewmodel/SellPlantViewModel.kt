package com.example.plantsservicefyp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellPlantViewModel @Inject constructor(val plantRepository: PlantRepository) : ViewModel() {

    private var observePlantAdd = MutableLiveData<UiState<Plant>>()

    val _observePlantAdd: LiveData<UiState<Plant>>
        get() = observePlantAdd

    fun addPlant (plant: Plant, imageUri: Uri) {
        plantRepository.addPlant(plant, imageUri) {
            observePlantAdd.value = it
        }
    }

}