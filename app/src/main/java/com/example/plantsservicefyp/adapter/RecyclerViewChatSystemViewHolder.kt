package com.example.plantsservicefyp.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.plantsservicefyp.databinding.AdminChatSystemRecyclerViewLayoutBinding
import com.example.plantsservicefyp.databinding.UserChatSystemRecyclerViewLayoutBinding
import com.example.plantsservicefyp.model.ChatSystemData

sealed class RecyclerViewChatSystemViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    class AdminChatViewHolder(private val binding: AdminChatSystemRecyclerViewLayoutBinding) :
        RecyclerViewChatSystemViewHolder(binding) {
        fun bind(botChat: ChatSystemData.AdminChatSystem) {
//            set views and on clicks here please
//            binding.chatBotTextView.text = botChat.body
        }
    }

    class UserChatViewHolder(private val binding: UserChatSystemRecyclerViewLayoutBinding) :
        RecyclerViewChatSystemViewHolder(binding) {
        fun bind(userChat: ChatSystemData.UserChatSystem) {
//            set views and on clicks here please
//            binding.chatUserTextView.text = userChat.body
        }
    }
}