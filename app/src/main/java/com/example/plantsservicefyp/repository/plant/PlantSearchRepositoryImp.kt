package com.example.plantsservicefyp.repository.plant

import android.content.Context
import android.util.Log
import com.example.plantsservicefyp.model.firebase.Cart
import com.example.plantsservicefyp.model.firebase.Favourite
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.constant.FirebaseConstants
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/*
* use document method in firestore when you are adding
* a value into firestore or you are searching a document
* by document id so than you should use document method
* with collection. If you are simply searching into firestore
* you should never use document property because it will mess
* up the search
* */

class PlantSearchRepositoryImp @Inject constructor(
    @ApplicationContext val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : PlantSearchRepository {

    override fun getAllPlants(callback: (UiState<List<DocumentSnapshot>>) -> Unit) {
        callback(UiState.Loading)
        firebaseFirestore.collection(FirebaseConstants.FIRESTORE_PLANT.value)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    callback(UiState.Success(it.documents))
                } else {
                    callback(UiState.Success(emptyList()))
                }
            }
            .addOnFailureListener {
                callback(UiState.Exception(it.message))
                Log.d("hm123", "exception: ${it.message}")
            }
    }

    override fun getApprovedPlants(callback: (UiState<List<DocumentSnapshot>>) -> Unit) {
        callback(UiState.Loading)
        firebaseFirestore.collection(FirebaseConstants.FIRESTORE_PLANT.value)
            .whereEqualTo("plantState", true)
            .get()
            .addOnSuccessListener {
                callback(UiState.Success(it.documents))
            }
            .addOnFailureListener {
                callback(UiState.Exception(it.message))
            }
    }

    override fun searchByCategory(
        searchCategory: String,
        callback: (UiState<List<DocumentSnapshot>>) -> Unit
    ) {
        callback(UiState.Loading)
        firebaseFirestore.collection(FirebaseConstants.FIRESTORE_PLANT.value)
            .whereEqualTo("plantCategory", searchCategory)
            .get()
            .addOnSuccessListener {
                callback(UiState.Success(it.documents))
            }
            .addOnFailureListener {
                callback(UiState.Exception(it.message))
            }
    }

    override fun searchByName(
        searchName: String,
        callback: (UiState<DocumentSnapshot>) -> Unit
    ) {
        callback(UiState.Loading)
        firebaseFirestore.collection(FirebaseConstants.FIRESTORE_PLANT.value)
            .whereEqualTo("name", searchName)
            .limit(1)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    callback(UiState.Success(it.first()))
                } else {
                    callback(UiState.Success(null))
                }
            }
            .addOnFailureListener {
                context.log("plant search: ${it.message.toString()}")
                callback(UiState.Exception(it.message))
            }
    }

    override fun editPlantState(plant: Plant, callback: (UiState<String>) -> Unit) {
        callback(UiState.Loading)
        firebaseFirestore.collection(FirebaseConstants.FIRESTORE_PLANT.value)
            .document(plant.plantId)
            .set(plant)
            .addOnSuccessListener {
                context?.log("edit: success")
                callback(UiState.Success(null))
            }
            .addOnFailureListener {
                callback(UiState.Exception(it.message))
                context?.log("edit exception: ${it.message}")
            }
    }

    override fun getCartItems(
        buyerId: String,
        plantList: (UiState<List<DocumentSnapshot>>) -> Unit,
        cartItems: (UiState<List<DocumentSnapshot>>) -> Unit
    ) {
        var plantList = mutableListOf<DocumentSnapshot>()
        lateinit var listCompleteCallBack: () -> Unit
        context?.log("started")
        context?.log("buyer id: ${buyerId}")
        plantList(UiState.Loading)
        firebaseFirestore
            .collection(FirebaseConstants.FIRESTORE_CART.value)
            .whereEqualTo("buyerId", buyerId)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    context?.log("cart items: ${it.documents.size}")
                    it.documents.forEach {
                        it.toObject(Cart::class.java).apply {
                            firebaseFirestore
                                .collection(FirebaseConstants.FIRESTORE_PLANT.value)
                                .whereEqualTo("plantId", this?.plantId)
                                .get()
                                .addOnSuccessListener {
                                    context?.log(it.documents.get(0).id.toString())
                                    plantList.add(it.documents.get(0))
                                    listCompleteCallBack()
                                }
                        }
                    }
                    listCompleteCallBack = {
                        if (it.documents.size == plantList.size) {
                            cartItems(UiState.Success(it.documents))
                            plantList(UiState.Success(plantList))
                        }
                    }
                } else {
                    cartItems(UiState.Success(emptyList()))
                    plantList(UiState.Success(emptyList()))
                }
            }
            .addOnFailureListener {
                plantList(UiState.Exception(it.message))
            }
    }

    override fun deleteCartItem(cartItem: DocumentSnapshot) {
        firebaseFirestore
            .collection(FirebaseConstants.FIRESTORE_CART.value)
            .document(cartItem.id)
            .delete()
            .addOnSuccessListener {
                context?.log("documented deleted")
            }
            .addOnFailureListener {
                context?.log("delete exception: ${it.message}")
            }
    }

    override fun deleteAllCartItems(cartItems: List<DocumentSnapshot>) {
        cartItems.forEach { cartItem ->
            firebaseFirestore
                .collection(FirebaseConstants.FIRESTORE_CART.value)
                .document(cartItem.id)
                .delete()
                .addOnSuccessListener {
                    context?.log("delete all id: ${cartItem.id}")
                }
        }
    }

    override fun getFavouriteItems(
        buyerId: String,
        plantListCallBack: (UiState<List<DocumentSnapshot>>) -> Unit,
        favouriteItems: (UiState<List<DocumentSnapshot>>) -> Unit
    ) {
        context?.log("buyer id: ${buyerId}")
        plantListCallBack(UiState.Loading)
        var plantList = mutableListOf<DocumentSnapshot>()
        lateinit var listCompleteCallBack: () -> Unit
        firebaseFirestore
            .collection(FirebaseConstants.FIRESTORE_FAVOURITE.value)
            .whereEqualTo("buyerId", buyerId)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    it.documents.forEach {
                        it.toObject(Favourite::class.java)?.apply {
                            context?.log("favourite from firebase: ${this.toString()}")
                            firebaseFirestore
                                .collection(FirebaseConstants.FIRESTORE_PLANT.value)
                                .whereEqualTo("plantId", plantId)
                                .get()
                                .addOnSuccessListener {
                                    if (!it.isEmpty) {
                                        plantList.add(it.documents.get(0))
                                        listCompleteCallBack()
                                    }
                                }
                        }
                    }
                    listCompleteCallBack = {
                        if (it.documents.size == plantList.size) {
                            favouriteItems(UiState.Success(it.documents))
                            plantList.forEach {
                                context?.log("favourite complete ${it.id}")
                            }
                            plantListCallBack(UiState.Success(plantList))
                        }
                    }
                } else {
                    plantListCallBack(UiState.Success(emptyList()))
                }
            }
            .addOnFailureListener {
                plantListCallBack(UiState.Exception(it.message))
            }
    }

    override fun deleteFavouriteItem(favouriteItem: DocumentSnapshot) {
        context?.log("delete favourite item: ${favouriteItem.id}")
        firebaseFirestore
            .collection(FirebaseConstants.FIRESTORE_FAVOURITE.value)
            .document(favouriteItem.id)
            .delete()
            .addOnSuccessListener {
            }
    }

    override fun favouriteContainsPlant(
        buyerId: String,
        plantId: String,
        callback: (DocumentSnapshot?) -> Unit
    ) {
        firebaseFirestore
            .collection(FirebaseConstants.FIRESTORE_FAVOURITE.value)
            .whereEqualTo("buyerId", buyerId)
            .whereEqualTo("plantId", plantId)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    callback(it.documents.get(0))
                } else {
                    callback(null)
                }
            }
    }

    override fun updatePlantsSold(plantsFromCart: List<DocumentSnapshot>) {
        plantsFromCart.forEachIndexed { index, plantDocument ->
            firebaseFirestore
                .collection(FirebaseConstants.FIRESTORE_PLANT.value)
                .document(plantDocument.id)
                .get()
                .addOnSuccessListener {
                    it.toObject(Plant::class.java)?.apply {
                        sold = sold?.plus(1)
                        firebaseFirestore
                            .collection(FirebaseConstants.FIRESTORE_PLANT.value)
                            .document(plantDocument.id)
                            .update("sold", sold)
                            .addOnSuccessListener {
                                context?.log("update sold updated: ${plantDocument.id}")
                            }
                    }
                }
        }
    }

}
