package com.example.plantsservicefyp.model

sealed class ChatSystemData {
    data class AdminChatSystem(
        val email: String,
        val message: String
    ) : ChatSystemData()

    data class UserChatSystem(
        val email: String,
        val message: String
    ) : ChatSystemData()
}
