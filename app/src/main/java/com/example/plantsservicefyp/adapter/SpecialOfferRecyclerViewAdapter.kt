package com.example.plantsservicefyp.adapter

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.model.Plant
import org.w3c.dom.Text

class SpecialOfferRecyclerViewAdapter(private val context: Context, private var data: List<Plant>) :
    RecyclerView.Adapter<SpecialOfferRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.special_offer_recycler_view_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.plantNameTextView.text = data.get(position).name
        viewHolder.plantPriceTextView.text = "Rs. ${data.get(position).price}"
        viewHolder.plantRatingTextView.text = data.get(position).rating
        viewHolder.plantSoldTextView.text = "${data.get(position).sold} sold"
        Glide
            .with(context)
            .load(Uri.parse(data.get(position).imageDownloadUrl))
            .centerCrop()
            .placeholder(R.drawable.baseline_file_download_24)
            .into(viewHolder.plantImageView);
    }

    override fun getItemCount() = data.size

    fun updateAdapter (data: List<Plant>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantImageView: ImageView = itemView.findViewById(R.id.specialOfferPlantImageView)
        val plantNameTextView: TextView = itemView.findViewById(R.id.plantNameTextView)
        val plantPriceTextView: TextView = itemView.findViewById(R.id.plantPriceTextView)
        val plantRatingTextView: TextView = itemView.findViewById(R.id.plantRatingTextView)
        val plantSoldTextView: TextView = itemView.findViewById(R.id.plantSoldTextView)
    }
}