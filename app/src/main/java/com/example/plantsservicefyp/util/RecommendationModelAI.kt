package com.example.plantsservicefyp.util

import android.content.Context
import android.util.Log
import com.example.plantsservicefyp.model.firebase.Plant
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class RecommendationModelAI @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val plantList: MutableList<Plant> = mutableListOf<Plant>()

    //    downloading tensorFlow model
    suspend fun downloadModel(modelName: String) {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel(modelName, DownloadType.LOCAL_MODEL, conditions)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    context.log("model successfully downloaded")
                } else {
                    context.log("model name: ${modelName}")
                    GlobalScope.launch { it.result }
                }
            }
            .addOnFailureListener {
                context.log("model failed for recommendation, check your connection!")
            }
    }

    /** Load recommendation plant list.  */
    suspend fun loadPlantList(plantList: List<DocumentSnapshot>, context: Context): List<DocumentSnapshot> {
        return suspendCancellableCoroutine<List<DocumentSnapshot>> { continuation ->
            val recommendedPlants = plantList.postProcessingCoefficientPlant(context)
            continuation.resume(recommendedPlants)
        }
    }

    @Synchronized
    suspend fun preProcess(selectedPlants: List<Plant>): IntArray {
        return withContext(Dispatchers.Default) {
            val inputContext = IntArray(plantList.size)
            for (i in 0 until plantList.size) {
                if (i < selectedPlants.size) {
                    val id = selectedPlants[i]
                    inputContext[i]
                } else {
                    // Padding input.
                    inputContext[i] = plantList.size
                }
            }
            inputContext
        }
    }

    /** postProcess to gets results from tensorFlowLite inference.  */
    @Synchronized
    suspend fun postProcess(
        outputIds: IntArray, confidences: FloatArray, selectedPlants: List<Plant>
    ): List<Plant> {
        return withContext(Dispatchers.Default) {
            val results = ArrayList<Plant>()
            // Add recommendation results. Filter null or contained items.
            for (i in outputIds.indices) {
                Log.v(
                    "hm123",
                    "Selected top K: %d. Ignore the rest."
                )
                val id = outputIds[i]
                val item = confidences.size
                if (item == null) {
                    Log.v("hm123", String.format("Inference output[%d]. Id: %s is null", i, id))
                    continue
                }
                if (!selectedPlants.isEmpty()) {
                    if (selectedPlants.contains(plantList[item])) {
                        Log.v(
                            "hm123",
                            String.format("Inference output[%d]. Id: %s is contained", i, id)
                        )
                        continue
                    }
                }
                val result = Plant()
                results.add(result)
                Log.v("hm123", String.format("Inference output[%d]. Result: %s", i, result))
            }
            results
        }
    }


}