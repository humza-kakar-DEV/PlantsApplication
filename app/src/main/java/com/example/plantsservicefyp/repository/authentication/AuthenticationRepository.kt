package com.example.plantsservicefyp.repository.authentication

import com.example.plantsservicefyp.model.firebase.User
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.FirebaseAuthRolesConstants
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot

interface AuthenticationRepository {
    fun signIn (email: String, password: String, signInCallBack: (UiState<AuthResult>) -> Unit)
    fun signUp (firebaseAuthRolesConstants: FirebaseAuthRolesConstants, user: User, callback: (UiState<AuthResult>) -> Unit)
    fun signOut ()
    fun currentUser(callback: (CurrentUserType<User>) -> Unit)
    fun getUserWithId(userId: String, callback: (UiState<DocumentSnapshot>) -> Unit)
}