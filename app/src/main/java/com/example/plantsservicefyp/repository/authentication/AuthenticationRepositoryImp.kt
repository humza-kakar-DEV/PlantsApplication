package com.example.plantsservicefyp.repository.authentication

import android.app.role.RoleManager
import android.content.Context
import android.media.MediaRouter.UserRouteInfo
import android.widget.Toast
import com.example.plantsservicefyp.model.User
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.constant.FirebaseAuthRolesConstants
import com.example.plantsservicefyp.util.constant.FirebaseConstants
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import hilt_aggregated_deps._com_example_plantsservicefyp_di_AuthenticationRepositoryModule
import hilt_aggregated_deps._com_example_plantsservicefyp_util_PlantsServiceFyp_GeneratedInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.callbackFlow
import org.checkerframework.checker.units.qual.Current
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.log
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

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
                        callback(UiState.Error("admin already exists!"))
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
                callback(UiState.Error(it.message))
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
                callback(UiState.Error(it.message))
            }
    }

    override fun currentUser(callback: (CurrentUserType<User>) -> Unit) {
        firebaseAuth.currentUser?:callback(CurrentUserType.Exception("user not founded!"))
        callback(CurrentUserType.Loading)
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

    override fun signOut() {
        firebaseAuth.signOut()
    }

}
