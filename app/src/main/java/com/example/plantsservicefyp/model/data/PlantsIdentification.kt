package com.example.apitesting.model.reponse

@kotlinx.serialization.Serializable
data class PlantsIdentification(
    val countable: Boolean,
    val custom_id: String?,
    val fail_cause: String?,
    val feedback: String?,
    val finished_datetime: Double,
    val id: Int,
    val images: List<Image>,
    val is_plant: Boolean,
    val is_plant_probability: Double,
    val meta_data: MetaData,
    val modifiers: List<String>,
    val secret: String,
    val suggestions: List<Suggestion>,
    val uploaded_datetime: Double
)