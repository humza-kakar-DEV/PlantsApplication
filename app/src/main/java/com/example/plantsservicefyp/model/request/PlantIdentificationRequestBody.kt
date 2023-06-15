package com.example.apitesting.model.request

@kotlinx.serialization.Serializable
data class PlantIdentificationRequestBody(
    val images: List<String>,
    val plant_details: List<String>,
    val plant_language: String,
)
