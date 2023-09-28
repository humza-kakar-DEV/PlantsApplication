package com.example.plantsservicefyp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plantsservicefyp.databinding.VideoListRecyclerViewLayoutBinding
import com.example.plantsservicefyp.model.VideoDetail
import com.example.plantsservicefyp.util.toast

class VideoListRecyclerViewAdapter(
    private val context: Context,
    private val callback: (position: Int) -> Unit,
) : RecyclerView.Adapter<VideoListRecyclerViewAdapter.ViewHolder>() {

    var items = listOf<VideoDetail>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: VideoListRecyclerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoListRecyclerViewAdapter.ViewHolder {
        val binding = VideoListRecyclerViewLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(
        holder: VideoListRecyclerViewAdapter.ViewHolder,
        position: Int
    ) {
        with(holder) {
            itemsLeftToRightAnimation(holder.itemView)
            items[position].apply {
                Glide
                    .with(context)
                    .load(thumbnailLink)
                    .centerCrop()
                    .into(binding.imageFilterView)
                binding.textView1.text = title
                binding.playButton.setOnClickListener {
                    callback(position)
                }
            }
        }
    }

    private fun itemsLeftToRightAnimation(itemView: View) {
        itemView.translationX = -100f
        itemView.alpha = 0f
        itemView.animate().translationX(0f).start()
        itemView.animate().alpha(1f).start()
    }

}