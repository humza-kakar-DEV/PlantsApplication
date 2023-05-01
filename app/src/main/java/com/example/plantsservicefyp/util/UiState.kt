package com.example.plantsservicefyp.util

import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories.FragmentEntryPoint

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Error(val exception: String?) : UiState<Nothing>()
    data class Success<T>(val data: T?) : UiState<T>()
    sealed class FirebaseAuthState<out T : FirebaseUser?> {
        object LoggedIn : FirebaseAuthState<FirebaseUser>()
        object LoggedOut : FirebaseAuthState<FirebaseUser>()
    }
}
