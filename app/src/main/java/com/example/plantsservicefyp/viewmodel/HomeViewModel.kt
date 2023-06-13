package com.example.plantsservicefyp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val plantSearchRepository: PlantSearchRepository,
    @ApplicationContext var context: Context,
) : ViewModel() {

    init {
    }

    private var _observePlantList = MutableLiveData<UiState<List<DocumentSnapshot>>>()

    val observePlantList: LiveData<UiState<List<DocumentSnapshot>>>
        get() = observePlantList

    fun getAllPlants() {
        plantSearchRepository.getAllPlants {
            context.log("home fragment: ${it}")
//            if (it is UiState.Success) {
//                _observePlantList.value = it
//            }
        }
    }

}