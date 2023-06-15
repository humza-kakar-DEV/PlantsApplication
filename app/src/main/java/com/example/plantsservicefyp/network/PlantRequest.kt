package com.example.plantsservicefyp
import com.example.apitesting.model.reponse.PlantsIdentification
import com.example.apitesting.model.request.PlantIdentificationRequestBody
import io.ktor.client.request.*
import java.util.StringTokenizer
import javax.inject.Inject

class PlantRequest @Inject constructor() {

//    use maps when you only want attributes
//    if you want to filter fields than use filter

    suspend fun plantDetails(images: List<String>): PlantsIdentification =
        KtorClient.httpClient.post {
            url("https://api.plant.id/v2/identify")
            body = PlantIdentificationRequestBody(images, listOf("wiki_description"), "en")
        }

}