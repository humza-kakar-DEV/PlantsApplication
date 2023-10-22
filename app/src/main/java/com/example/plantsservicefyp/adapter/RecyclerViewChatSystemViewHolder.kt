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
        fun bind(adminChat: ChatSystemData.AdminChatSystem) {
//            set views and on clicks here please
            binding.emailTextView.text = adminChat.email.toString()
            binding.messageTextView.text = adminChat.message.toString()
        }
    }

    class UserChatViewHolder(private val binding: UserChatSystemRecyclerViewLayoutBinding) :
        RecyclerViewChatSystemViewHolder(binding) {
        fun bind(userChat: ChatSystemData.UserChatSystem) {
//            set views and on clicks here please
            binding.emailTextView.text = userChat.email.toString()
            binding.messageTextView.text = userChat.message.toString()
        }
    }
}