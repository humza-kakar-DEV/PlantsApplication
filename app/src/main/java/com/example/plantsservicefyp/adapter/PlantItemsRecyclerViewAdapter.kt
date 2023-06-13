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
import com.google.firebase.firestore.DocumentSnapshot

class PlantItemsRecyclerView (
    private val context: Context,
    var callback: (selectedPlant: DocumentSnapshot) -> Unit,
) : RecyclerView.Adapter<PlantItemsRecyclerView.ViewHolder>() {

    private var plantDocumentList = mutableListOf<DocumentSnapshot>()
    private var plantList = mutableListOf<Plant>()

    init {
        convertToPlant()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.special_offer_recycler_view_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.plantNameTextView.text = plantList.get(position).name
        viewHolder.plantPriceTextView.text = "Rs. ${plantList.get(position).price}"
        viewHolder.plantRatingTextView.text = plantList.get(position).rating
        viewHolder.plantSoldTextView.text = "${plantList.get(position).sold} sold"
        Glide
            .with(context)
            .load(Uri.parse(plantList.get(position).imageDownloadUrl))
            .centerCrop()
            .placeholder(R.drawable.baseline_file_download_24)
            .into(viewHolder.plantImageView)
        viewHolder.plantImageView.setOnClickListener {
            Toast.makeText(context, "open show item screen", Toast.LENGTH_SHORT).show()
            callback(plantDocumentList.get(position))
        }
    }

    override fun getItemCount() = plantList.size

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

    fun convertToPlant () {
        plantDocumentList.forEach {
            plantList.add(it.toObject(Plant::class.java)!!)
        }
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