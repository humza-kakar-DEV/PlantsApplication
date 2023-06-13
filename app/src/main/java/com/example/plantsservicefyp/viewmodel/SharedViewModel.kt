package com.example.plantsservicefyp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val plantSearchRepository: PlantSearchRepository,
    private @ApplicationContext val context: Context
) : ViewModel() {

    private var observeChangeFragment = MutableLiveData<ChangeFragment>()
    val _observeChangeFragment: LiveData<ChangeFragment>
        get() = observeChangeFragment

    private var observeSelectedPlant = MutableLiveData<DocumentSnapshot?>()
    val _observeSelectedPlant: LiveData<DocumentSnapshot?>
        get() = observeSelectedPlant

    private var observeTotalPrice = MutableLiveData<String>()
    val _observeTotalPrice: LiveData<String>
        get() = observeTotalPrice

    fun changeFragment(_changeFragment: ChangeFragment) {
        observeChangeFragment.value = _changeFragment
    }

    fun setSelectedPlant(selectedPlant: DocumentSnapshot?) {
        observeSelectedPlant.value = selectedPlant
    }

    fun searchByName(plantName: String) {
        observeSelectedPlant.value = null
        plantSearchRepository.searchByName(plantName) {
            if (it is UiState.Success) {
                observeSelectedPlant.value = it.data
            }
        }
    }

    fun setTotalPrice (price: String) {
        observeTotalPrice.value = price
    }

}