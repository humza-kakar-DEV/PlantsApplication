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
import com.example.plantsservicefyp.model.firebase.Plant
import com.google.apphosting.datastore.testing.DatastoreTestTrace.FirestoreV1Action.ListDocuments
import com.google.firebase.firestore.DocumentSnapshot

class ApprovalRecyclerViewAdapter (
    private val context: Context,
    var callback: (selectedPlant: DocumentSnapshot) -> Unit,
) : RecyclerView.Adapter<ApprovalRecyclerViewAdapter.ViewHolder>() {

    private var approvalDocumentList = mutableListOf<DocumentSnapshot>()
    private var approvalList = mutableListOf<Plant>()

    init {
        convertToPlant()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.approval_recyclerview_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    }

    override fun getItemCount() = approvalList.size

    fun updateAdapterWithDocuments(approvalDocumentList: List<DocumentSnapshot>) {
        this.approvalDocumentList.clear()
        this.approvalDocumentList.addAll(approvalDocumentList)
        convertToPlant()
    }

    private fun convertToPlant () {
        approvalList.clear()
        approvalDocumentList.forEach {
            approvalList.add(it.toObject(Plant::class.java)!!)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val plantImageView: ImageView = itemView.findViewById(R.id.specialOfferPlantImageView)
//        val plantNameTextView: TextView = itemView.findViewById(R.id.plantNameTextView)
//        val plantPriceTextView: TextView = itemView.findViewById(R.id.plantPriceTextView)
//        val plantRatingTextView: TextView = itemView.findViewById(R.id.plantRatingTextView)
//        val plantSoldTextView: TextView = itemView.findViewById(R.id.plantSoldTextView)
    }
}