package com.example.plantsservicefyp.model.firebase

data class Message(
    var messageId: String = "",
    var email: String = "",
    var message: String = "",
    var messageType: String = "",
    var createdAt: Long = 0L,
)
