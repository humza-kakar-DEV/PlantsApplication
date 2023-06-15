package com.example.plantsservicefyp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apitesting.model.reponse.PlantsIdentification
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SellPlantViewModel @Inject constructor(
    val plantRepository: PlantRepository,
    private @ApplicationContext var context: Context
) : ViewModel() {

    private var observePlantAdd = MutableLiveData<UiState<Plant>>()
    val _observePlantAdd: LiveData<UiState<Plant>>
        get() = observePlantAdd

    private var observePlantIndentification = MutableLiveData<UiState<PlantsIdentification>>()
    val _observePlantIndentification: LiveData<UiState<PlantsIdentification>>
        get() = observePlantIndentification

    fun addPlant(plant: Plant, imageUri: Uri) {
        plantRepository.addPlant(plant, imageUri) {
            observePlantAdd.value = it
        }
    }

    fun indentifyPlant(imageList: List<Uri>) {
        observePlantIndentification.value = UiState.Loading
        viewModelScope.launch {
            try {
                observePlantIndentification.value = UiState.Success(plantRepository.indentifyPlant(imageList))
            } catch (e: Exception) {
                observePlantIndentification.value = UiState.Exception(e.message.toString())
            }
        }
    }

}