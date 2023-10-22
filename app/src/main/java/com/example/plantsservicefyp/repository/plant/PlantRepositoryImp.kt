package com.example.plantsservicefyp.repository.plant

import android.content.Context
import android.net.Uri
import android.os.Build
import android.service.notification.NotificationListenerService.Ranking
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.apitesting.model.reponse.PlantsIdentification
import com.example.plantsservicefyp.PlantRequest
import com.example.plantsservicefyp.model.firebase.Cart
import com.example.plantsservicefyp.model.firebase.Favourite
import com.example.plantsservicefyp.model.firebase.Message
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.constant.FirebaseConstants
import com.example.plantsservicefyp.util.ImageMimeType
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.base64EncodeFromUri
import com.example.plantsservicefyp.util.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firestore.v1.DocumentTransform.FieldTransform.ServerValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepositoryImp @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val imageMimeType: ImageMimeType,
    private val plantRequest: PlantRequest,
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
                        callback(UiState.Exception(it.message))
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
                val imageDownloadUrl = task.result
                callback(imageDownloadUrl)
            } else {
//                failure while uploading
                context.log("image upload error: ${task.exception.toString()}")
            }
        }
    }

    override fun addItemToCart(cart: Cart) {
        cart.apply {
            cartId =
                firebaseFirestore.collection(FirebaseConstants.FIRESTORE_CART.value).document().id
            firebaseFirestore.collection(FirebaseConstants.FIRESTORE_CART.value)
                .document(cartId)
                .set(cart)
                .addOnFailureListener {
                    Toast.makeText(context, "error ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun addItemToFavourite(favourite: Favourite) {
        favourite.apply {
            favouriteId = firebaseFirestore
                .collection(FirebaseConstants.FIRESTORE_FAVOURITE.value)
                .document().id
            firebaseFirestore.collection(FirebaseConstants.FIRESTORE_FAVOURITE.value)
                .document(favouriteId)
                .set(favourite)
                .addOnFailureListener {
                    Toast.makeText(context, "error ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun identifyPlant(imageList: List<Uri>): PlantsIdentification =
        withContext(Dispatchers.IO) {
            val image64List = mutableListOf<String>()
            imageList.forEach {
                it.base64EncodeFromUri(
                    context
                )?.let {
                    image64List.add(it)
                }
            }
            plantRequest.plantDetails(image64List)
        }

    override fun writeMessage(message: Message): Flow<Unit> {
        return flow {
            message.apply {
                messageId = firebaseFirestore.collection("maintenance_customer").document().id
                firebaseFirestore
                    .collection("maintenance_customer")
                    .document(messageId)
                    .set(this)
                    .await()
            }
        }
    }

    override fun getAllMessages(): Flow<List<Message>> {
        return callbackFlow {
            val snapshotListener =
                firebaseFirestore.collection("maintenance_customer")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            close(e)
                            return@addSnapshotListener
                        }
                        if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                            val data = mutableListOf<Message>()
                            if (snapshot.documents.isNotEmpty()) {
                                snapshot.documents.forEach {
                                    data.add(it.toObject(Message::class.java)!!)
                                }
                                trySend(data)
                            } else {
                                context.log("message list is empty!")
                                trySend(emptyList())
                            }
                        }
                    }
            awaitClose {
                snapshotListener.remove()
            }
        }
    }

}