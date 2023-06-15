package com.example.apitesting.model.reponse

@kotlinx.serialization.Serializable
data class StructuredName(
    val genus: String,
    val species: String
)