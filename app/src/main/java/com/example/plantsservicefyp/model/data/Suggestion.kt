package com.example.apitesting.model.reponse

@kotlinx.serialization.Serializable
data class Suggestion(
    val confirmed: Boolean,
    val id: Int,
    val plant_details: PlantDetails,
    val plant_name: String,
    val probability: Double
)