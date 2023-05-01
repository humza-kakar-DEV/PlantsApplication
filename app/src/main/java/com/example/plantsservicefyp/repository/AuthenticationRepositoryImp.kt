package com.example.plantsservicefyp.repository

import com.example.plantsservicefyp.model.User
import com.example.plantsservicefyp.util.Constants
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthenticationRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthenticationRepository {

    override fun signIn(
        email: String,
        password: String,
        signInCallBack: (UiState<AuthResult>?) -> Unit
    ) {
        signInCallBack(UiState.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                signInCallBack(UiState.Success(it))
            }
            .addOnFailureListener {
                signInCallBack(UiState.Error(it.message))
            }
    }

    override fun signUp(user: User, signUpCallback: (UiState<AuthResult>?) -> Unit) {
        signUpCallback(UiState.Loading)
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                signUpFirestore(user.apply {
                    id = it.user?.uid!!
                }) {
                    signUpCallback(UiState.Success(it))
                }
            }
            .addOnFailureListener {
                signUpCallback(UiState.Error(it.message))
            }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun currentUser(): UiState.FirebaseAuthState<FirebaseUser> {
        return  if (firebaseAuth.currentUser != null)
                    UiState.FirebaseAuthState.LoggedIn
                else
                    UiState.FirebaseAuthState.LoggedOut
    }

    private fun signUpFirestore(user: User, signUpFirestoreCallBack: () -> Unit) {
        firebaseFirestore
            .collection(Constants.FIRESTORE_AUTH.value)
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                signUpFirestoreCallBack()
            }
    }

}