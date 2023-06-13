package com.example.plantsservicefyp.util

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User

sealed class CurrentUserType<out User> {
    data class Admin<User>(val user: User): CurrentUserType<User>()
    data class Seller<User>(val user: User): CurrentUserType<User>()
    data class Buyer<User>(val user: User): CurrentUserType<User>()
    object Loading: CurrentUserType<Nothing>()
    data class Exception(val error: String): CurrentUserType<Nothing>()
}
