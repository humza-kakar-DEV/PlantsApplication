package com.example.plantsservicefyp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.media3.exoplayer.source.BehindLiveWindowException
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.AdminChatSystemRecyclerViewLayoutBinding
import com.example.plantsservicefyp.databinding.UserChatSystemRecyclerViewLayoutBinding
import com.example.plantsservicefyp.model.ChatSystemData


class RecyclerViewChatSystemAdapter(
    private val context: Context,
    private val callback: () -> Unit,
) : RecyclerView.Adapter<RecyclerViewChatSystemViewHolder>() {

    var items = listOf<ChatSystemData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewChatSystemViewHolder {
        return when (viewType) {
            R.layout.admin_chat_system_recycler_view_layout -> RecyclerViewChatSystemViewHolder.AdminChatViewHolder(
                AdminChatSystemRecyclerViewLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            R.layout.user_chat_system_recycler_view_layout -> RecyclerViewChatSystemViewHolder.UserChatViewHolder(
                UserChatSystemRecyclerViewLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onViewAttachedToWindow(holder: RecyclerViewChatSystemViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: RecyclerViewChatSystemViewHolder, position: Int) {
        when (holder) {
            is RecyclerViewChatSystemViewHolder.AdminChatViewHolder -> {
                itemsLeftToRightAimation(holder.itemView)
                holder.bind(items[position] as ChatSystemData.AdminChatSystem)
            }

            is RecyclerViewChatSystemViewHolder.UserChatViewHolder -> {
                itemsLeftToRightAimation(holder.itemView)
                holder.bind(items[position] as ChatSystemData.UserChatSystem)
            }
        }
    }

    private fun itemsLeftToRightAimation(itemView: View) {
        itemView.translationX = -100f
        itemView.alpha = 0f
        itemView.animate().translationX(0f).start()
        itemView.animate().alpha(1f).start()
    }

    private fun itemsRightToLeftAnimation(itemView: View) {
        itemView.translationX = 100f
        itemView.alpha = 0f
        itemView.animate().translationX(0f).start()
        itemView.animate().alpha(1f).start()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ChatSystemData.AdminChatSystem -> R.layout.admin_chat_system_recycler_view_layout
            is ChatSystemData.UserChatSystem -> R.layout.user_chat_system_recycler_view_layout
        }
    }
}