package com.example.plantsservicefyp.model

import android.net.Uri

data class Plant (
    var plantId: String = "",
    var sellerId: String = "",
    var name: String? = "",
    var description: String? = "",
    var plantCategory: String = "",
    var price: String? = "",
    var location: String? = "",
    var rating: String? = "",
    var sold: String? = "",
    var imageDownloadUrl: String? = null,
)
