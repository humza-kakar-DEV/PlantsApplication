package com.example.plantsservicefyp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantsservicefyp.model.firebase.User
import com.example.plantsservicefyp.repository.authentication.AuthenticationRepository
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.FirebaseAuthRolesConstants
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private var observeSignUp = MutableLiveData<UiState<AuthResult>>()
    val _observeSignUp: LiveData<UiState<AuthResult>>
        get() = observeSignUp

    private var observeSignIn = MutableLiveData<UiState<AuthResult>>()
    val _observeSignIn: LiveData<UiState<AuthResult>>
        get() = observeSignIn

    private var observeCurrentUser = MutableLiveData<CurrentUserType<User>>()
    val _observeCurrentUser: LiveData<CurrentUserType<User>>
        get() {
            currentUser()
            return observeCurrentUser
        }

    private var observeUser = MutableLiveData<UiState<DocumentSnapshot>>()
    val _observeUser: LiveData<UiState<DocumentSnapshot>>
        get() = observeUser

    fun signIn(email: String, password: String) {
        authenticationRepository.signIn(email, password) {
            observeSignIn.value = it
        }
    }

    fun signUp(firebaseAuthRolesConstants: FirebaseAuthRolesConstants, user: User) {
        authenticationRepository.signUp(firebaseAuthRolesConstants, user) {
            observeSignUp.value = it
        }
    }

    fun currentUser() {
        authenticationRepository.currentUser {
            observeCurrentUser.value = it
        }
    }

    fun signOut() {
        authenticationRepository.signOut()
    }

    fun getUserWithId(userId: String) {
        authenticationRepository.getUserWithId(userId) {
            observeUser.value = it
        }
    }

}