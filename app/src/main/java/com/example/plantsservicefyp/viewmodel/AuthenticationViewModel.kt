package com.example.plantsservicefyp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.model.User
import com.example.plantsservicefyp.repository.AuthenticationRepository
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject() constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private var observeSignIn = MutableLiveData<UiState<AuthResult>>()

    val _observeSignIn: LiveData<UiState<AuthResult>>
        get() = observeSignIn

    private var observeSignUp = MutableLiveData<UiState<AuthResult>>()

    val _observeSignUp: LiveData<UiState<AuthResult>>
        get() = observeSignUp

    private var observeCurrentUser = MutableLiveData<UiState.FirebaseAuthState<FirebaseUser>>()

    val _observeCurrentUser: LiveData<UiState.FirebaseAuthState<FirebaseUser>>
        get() = observeCurrentUser

    fun signIn(email: String, password: String) {
        authenticationRepository.signIn(email, password) {
            observeSignIn.value = it
        }
    }

    fun signUp(user: User) {
        authenticationRepository.signUp(user) {
            observeSignUp.value = it
        }
    }

    fun currentUser() {
        observeCurrentUser.value = authenticationRepository.currentUser()
    }

    fun signOut() {
        authenticationRepository.signOut()
    }

}