package com.example.plantsservicefyp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.Constants
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class PlantSearchRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : PlantSearchRepository {

    override fun getAllPlants(callback: (UiState<List<Plant>>) -> Unit) {
        callback(UiState.Loading)
        firebaseFirestore.collection(Constants.FIRESTORE_PLANT.value)
            .get()
            .addOnSuccessListener {
                var plantList = mutableListOf<Plant>()
                for (document in it) {
                    var plant = document.toObject<Plant>()
                    plantList.add(plant)
                }
                callback(UiState.Success<List<Plant>>(plantList))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message))
                Log.d("hm123", "exception: ${it.message}")
            }
    }
}
