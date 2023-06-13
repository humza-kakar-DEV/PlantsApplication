package com.example.plantsservicefyp.repository.plant

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.plantsservicefyp.model.Cart
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.constant.FirebaseConstants
import com.example.plantsservicefyp.util.ImageMimeType
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepositoryImp @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val imageMimeType: ImageMimeType,
    @ApplicationContext val context: Context,
) : PlantRepository {

    override fun addPlant(plant: Plant, imageUri: Uri, callback: (UiState<Plant>?) -> Unit) {
        callback(UiState.Loading)
        uploadImage(imageUri = imageUri) { imageDownloadUrl ->
            plant.apply {
                this.imageDownloadUrl = imageDownloadUrl.toString()
                plantId = firebaseFirestore
                    .collection(FirebaseConstants.FIRESTORE_PLANT.value)
                    .document()
                    .id
                firebaseFirestore.collection(FirebaseConstants.FIRESTORE_PLANT.value)
                    .document(plantId)
                    .set(plant)
                    .addOnSuccessListener {
                        callback(UiState.Success(plant))
                    }
                    .addOnFailureListener {
                        callback(UiState.Error(it.message))
                    }
            }
        }
    }

    private fun uploadImage(imageUri: Uri, callback: (imageDownLoadUrl: Uri) -> Unit) {
        val imageReference = firebaseStorage.reference.child(
            "images/${System.currentTimeMillis()}.${
                ImageMimeType().getMimeType(
                    context = context,
                    uri = imageUri
                )
            }"
        )

        val uploadTask = imageReference.putFile(imageUri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                context.log("image upload success")
                val imageDownloadUrl = task.result
                callback(imageDownloadUrl)
            } else {
//                failure while uploading
                context.log("image upload error: ${task.exception.toString()}")
            }
        }
    }

    override fun addItemToCart(cart: Cart) {
        firebaseFirestore.collection(FirebaseConstants.FIRESTORE_CART.value)
            .document()
            .set(cart)
            .addOnFailureListener {
                Toast.makeText(context, "error ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}