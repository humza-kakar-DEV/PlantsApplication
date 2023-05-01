package com.example.plantsservicefyp.model

import android.net.Uri

data class Plant (
    var name: String? = "",
    var description: String? = "",
    var price: String? = "",
    var location: String? = "",
    var userId: String? = "",
    var rating: String? = "",
    var sold: String? = "",
    var imageDownloadUrl: String? = null,
)
