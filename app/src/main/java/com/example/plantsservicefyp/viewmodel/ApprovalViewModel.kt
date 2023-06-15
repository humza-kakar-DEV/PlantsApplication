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
class ApprovalViewModel @Inject constructor(
    private val plantSearchRepository: PlantSearchRepository,
    private @ApplicationContext var context: Context,
): ViewModel() {

    private var observeAllPlants = MutableLiveData<UiState<List<DocumentSnapshot>>>()
    val _observeAllPlants: LiveData<UiState<List<DocumentSnapshot>>>
        get() {
            getAllPlants()
            return observeAllPlants
        }

    fun getAllPlants() {
        plantSearchRepository.getAllPlants {
            observeAllPlants.value = it
        }
    }

}