package com.example.plantsservicefyp.repository

import com.example.plantsservicefyp.model.User
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface AuthenticationRepository {
    fun signIn (email: String, password: String, signInCallBack: (UiState<AuthResult>?) -> Unit)
    fun signUp (user: User, signUpCallback: (UiState<AuthResult>?) -> Unit)
    fun signOut ()
    fun currentUser(): UiState.FirebaseAuthState<FirebaseUser>
}