package com.example.plantsservicefyp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

    private var observeBoughtItems = MutableLiveData<List<DocumentSnapshot>>()
    val _observeBoughtItems: LiveData<List<DocumentSnapshot>>
        get() = observeBoughtItems

    private var observeSellerUser = MutableLiveData<DocumentSnapshot>()
    val _observeSellerUser: LiveData<DocumentSnapshot>
        get() = observeSellerUser

    private var observeBackToHomeTab = MutableLiveData<Boolean>()
    val _observeBackToHomeTab: LiveData<Boolean>
        get() = observeBackToHomeTab

    private var observeDestoryFragment = MutableLiveData<Boolean>()
    val _observeDestroyFragment: LiveData<Boolean>
        get() = observeDestoryFragment

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

    fun setTotalPrice(price: String) {
        observeTotalPrice.value = price
    }

    fun setBoughtItems(boughtItems: List<DocumentSnapshot>) {
        observeBoughtItems.value = boughtItems
    }

    fun setObserveSellerUser (user: DocumentSnapshot) {
        observeSellerUser.value = user
    }

    fun setBackToHomeTab (isBack: Boolean) {
        observeBackToHomeTab.value = true
    }

    fun destroyFragment (state: Boolean) {
        observeDestoryFragment.value = state
    }

}