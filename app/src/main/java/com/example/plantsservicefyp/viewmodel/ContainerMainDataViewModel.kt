package com.example.plantsservicefyp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hilt_aggregated_deps._com_example_plantsservicefyp_di_PlantSearchRepositoryModule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContainerMainDataViewModel @Inject constructor(
    private var plantSearchRepository: PlantSearchRepository,
    private @ApplicationContext var context: Context,
) : ViewModel() {

}