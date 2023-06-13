package com.example.plantsservicefyp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.toast
import com.google.firebase.firestore.DocumentSnapshot

class CartRecyclerViewAdapter(
    private val context: Context,
    private val callback: (Int) -> Unit,
) : RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder>() {

    private var plantDocumentList = mutableListOf<DocumentSnapshot>()
    private var plantList = mutableListOf<Plant>()

    init {
        convertToPlant()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cart_recycler_view_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        plantList.get(position).apply {
            Glide
                .with(context)
                .load(Uri.parse(imageDownloadUrl))
                .centerCrop()
                .placeholder(R.drawable.baseline_file_download_24)
                .into(viewHolder.plantImageView)
            viewHolder.plantNameTextView.text = name
            viewHolder.plantDescriptionTextView.text = description
            viewHolder.plantPriceTextView.text = "Rs. ${price}"
            viewHolder.plantImageView.setOnClickListener {
                callback(position)
            }
//            viewHolder.plantQuantityTextView.text = this.sold
        }
    }

    override fun getItemCount() = plantDocumentList.size

    fun updateAdapterWithDocuments(plantDocumentList: List<DocumentSnapshot>) {
        this.plantDocumentList.clear()
        this.plantDocumentList.addAll(plantDocumentList)
        convertToPlant()
    }

    fun updateWithPlants(plantList: List<Plant>) {
        this.plantList.clear()
        this.plantList.addAll(plantList)
        notifyDataSetChanged()
    }

    private fun convertToPlant () {
        plantDocumentList.forEach {
            plantList.add(it.toObject(Plant::class.java)!!)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantImageView: ImageView = itemView.findViewById(R.id.cartRecyclerViewImageView)
        val plantNameTextView: TextView = itemView.findViewById(R.id.cartRecyclerViewNameTextView)
        val plantDescriptionTextView: TextView = itemView.findViewById(R.id.cartRecyclerViewDescriptionTextView)
        val plantPriceTextView: TextView = itemView.findViewById(R.id.cartRecyclerViewPriceTextView)
//        val plantQuantityTextView: TextView = itemView.findViewById(R.id.cartRecyclerViewQuantityTextView)
    }
}