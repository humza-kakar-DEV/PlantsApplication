package com.example.plantsservicefyp.model.firebase

import android.net.Uri

data class Plant (
    var plantId: String = "",
    var sellerId: String = "",
    var name: String? = "",
    var description: String? = "",
    var plantCategory: String = "",
    var price: String? = "",
    var location: String? = "",
    var imageDownloadUrl: String? = null,
    var plantState: Boolean? = null,
    var rating: Float? = 0f,
    var sold: Int? = 0,
)
