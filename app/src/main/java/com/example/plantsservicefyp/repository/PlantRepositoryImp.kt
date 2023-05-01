package com.example.plantsservicefyp.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.Constants
import com.example.plantsservicefyp.util.ImageMimeType
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

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
                this.userId = firebaseAuth.currentUser?.uid
                this.imageDownloadUrl = imageDownloadUrl.toString()
            }
            firebaseFirestore.collection(Constants.FIRESTORE_PLANT.value)
                .document()
                .set(plant)
                .addOnSuccessListener {
                    callback(UiState.Success(plant))
                }
                .addOnFailureListener {
                    callback(UiState.Error(it.message))
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
                val imageDownloadUrl = task.result
                callback(imageDownloadUrl)
            } else {
//                failure while uploading
            }
        }
    }

}