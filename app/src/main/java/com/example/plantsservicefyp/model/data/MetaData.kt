package com.example.apitesting.model.reponse

import kotlinx.serialization.descriptors.PrimitiveKind

@kotlinx.serialization.Serializable
data class MetaData(
    val date: String,
    val datetime: String,
    val latitude: String?,
    val longitude: String?
)