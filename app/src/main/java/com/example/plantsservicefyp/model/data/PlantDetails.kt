package com.example.apitesting.model.reponse

@kotlinx.serialization.Serializable
data class PlantDetails(
    val language: String,
    val scientific_name: String,
    val structured_name: StructuredName,
    val wiki_description: WikiDescription
)