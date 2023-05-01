package com.example.plantsservicefyp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.util.ChangeFragment
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private var observeChangeFragment = MutableLiveData<ChangeFragment>()

    val _observeChangeFragment: LiveData<ChangeFragment>
        get() = observeChangeFragment

    fun changeFragment(_changeFragment: ChangeFragment) {
        observeChangeFragment.value = _changeFragment
    }

}