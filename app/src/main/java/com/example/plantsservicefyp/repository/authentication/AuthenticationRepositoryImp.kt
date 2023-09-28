package com.example.plantsservicefyp.repository.authentication

import android.content.Context
import com.example.plantsservicefyp.model.firebase.User
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.FirebaseAuthRolesConstants
import com.example.plantsservicefyp.util.constant.FirebaseConstants
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthenticationRepositoryImp @Inject constructor(
    @ApplicationContext var context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthenticationRepository {

    override fun signUp(
        firebaseAuthRolesConstants: FirebaseAuthRolesConstants,
        user: User,
        callback: (UiState<AuthResult>) -> Unit
    ) {
        callback(UiState.Loading)
        val reference = firebaseFirestore.collection(FirebaseConstants.FIRESTORE_USER.value)
            .document(FirebaseConstants.FIRESTORE_ROLES.value)
            .collection(firebaseAuthRolesConstants.value)
        when (firebaseAuthRolesConstants) {
            FirebaseAuthRolesConstants.FIRESTORE_ADMIN -> {
                reference.get().addOnSuccessListener {
                    if (it.isEmpty) {
                        createUser(reference, user) {
                            callback(it)
                        }
                    } else {
                        callback(UiState.Exception("admin already exists!"))
                        return@addOnSuccessListener
                    }
                }
            }
            else -> {
                createUser(reference, user) {
                    callback(it)
                }
            }
        }
    }

    private fun createUser(
        reference: CollectionReference,
        user: User,
        callback: (UiState<AuthResult>) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    val id = reference.document().id
                    user.apply {
                        userId = id
                    }
                    reference
                        .document(user.userId)
                        .set(user)
                        .addOnSuccessListener {
                            callback(UiState.Success(authResult.result))
                        }
                }
            }
            .addOnFailureListener {
                context.toast("😔 ${it.message}")
                callback(UiState.Exception(it.message))
            }
    }

    override fun signIn(
        email: String,
        password: String,
        callback: (UiState<AuthResult>) -> Unit
    ) {
        callback(UiState.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(UiState.Success(it))
            }
            .addOnFailureListener {
                callback(UiState.Exception(it.message))
            }
    }

    override fun currentUser(callback: (CurrentUserType<User>) -> Unit) {
        callback(CurrentUserType.Loading)
        if (firebaseAuth.currentUser == null) {
            callback(CurrentUserType.Exception("firebase auth user not found!!"))
        } else {
            FirebaseAuthRolesConstants.values().forEachIndexed { index, role ->
                firebaseFirestore.collection(FirebaseConstants.FIRESTORE_USER.value)
                    .document(FirebaseConstants.FIRESTORE_ROLES.value)
                    .collection(role.value)
                    .whereEqualTo("email", firebaseAuth.currentUser?.email)
                    .get()
                    .addOnSuccessListener {
                        it.documents.forEach {
                            when (role) {
                                FirebaseAuthRolesConstants.FIRESTORE_ADMIN -> {
                                    callback(CurrentUserType.Admin(it.toObject(User::class.java)!!))
                                }
                                FirebaseAuthRolesConstants.FIRESTORE_BUYER -> {
                                    callback(CurrentUserType.Buyer(it.toObject(User::class.java)!!))
                                }
                                FirebaseAuthRolesConstants.FIRESTORE_SELLER ->
                                    callback(CurrentUserType.Seller(it.toObject(User::class.java)!!))
                            }
                        }
                    }

            }
        }
    }

    override fun getUserWithId(
        userId: String,
        callback: (UiState<DocumentSnapshot>) -> Unit
    ) {
        callback(UiState.Loading)
        firebaseFirestore.collection(FirebaseConstants.FIRESTORE_USER.value)
            .document(FirebaseConstants.FIRESTORE_ROLES.value)
            .collection(FirebaseAuthRolesConstants.FIRESTORE_SELLER.value)
            .document(userId)
            .get()
            .addOnSuccessListener {
                callback(UiState.Success(it))
            }
            .addOnFailureListener {
                callback(UiState.Exception(it.message))
            }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

}
