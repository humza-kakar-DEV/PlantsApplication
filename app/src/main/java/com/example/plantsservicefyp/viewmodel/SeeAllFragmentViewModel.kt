package com.example.plantsservicefyp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.repository.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeeAllFragmentViewModel @Inject constructor(
    private val plantSearchRepository: PlantSearchRepository
) : ViewModel() {

    private var observePlantList = MutableLiveData<UiState<List<Plant>>>()

    val _observePlantList: LiveData<UiState<List<Plant>>>
        get() = observePlantList

    fun getAllPlants() {
        plantSearchRepository.getAllPlants {
            observePlantList.value = it
        }
    }

}