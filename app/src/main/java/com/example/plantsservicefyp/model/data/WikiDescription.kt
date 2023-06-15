package com.example.apitesting.model.reponse

@kotlinx.serialization.Serializable
data class WikiDescription(
    val citation: String,
    val license_name: String,
    val license_url: String,
    val value: String
)