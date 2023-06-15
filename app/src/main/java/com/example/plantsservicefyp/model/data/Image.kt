package com.example.apitesting.model.reponse

@kotlinx.serialization.Serializable
data class Image(
    val file_name: String,
    val url: String
)