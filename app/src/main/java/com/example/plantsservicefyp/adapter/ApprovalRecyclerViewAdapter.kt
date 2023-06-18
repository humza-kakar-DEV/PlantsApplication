package com.example.plantsservicefyp.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.alterText
import com.example.plantsservicefyp.util.animateHorizontalShake
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentSnapshot
import com.makeramen.roundedimageview.RoundedImageView

class ApprovalRecyclerViewAdapter (
    private val context: Context,
    var plantStateCallback: (statePlant: Plant) -> Unit,
    var plantMoreDetailsCallback: (detailedPlant: DocumentSnapshot) -> Unit
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
        approvalList.get(position).apply {
            viewHolder.nameTextView.text = name
            viewHolder.descriptionTextView.text = context.alterText(description!!)
            viewHolder.priceTextView.text = "Rs. ${price}"
            Glide
                .with(context)
                .load(Uri.parse(imageDownloadUrl))
                .centerCrop()
                .placeholder(R.drawable.baseline_file_download_24)
                .into(viewHolder.roundImage)
            viewHolder.approvalCheckBox.isChecked = plantState!!
            if (viewHolder.approvalCheckBox.isChecked) {
                viewHolder.materialCardView.strokeWidth = 3
                viewHolder.materialCardView.strokeColor = context.getResources().getColor(R.color.custom_green)
            } else {
                viewHolder.materialCardView.strokeWidth = 3
                viewHolder.materialCardView.strokeColor = context.getResources().getColor(R.color.white)
            }
            viewHolder.approvalCheckBox.setOnClickListener {
                plantState = viewHolder.approvalCheckBox.isChecked
                plantStateCallback(this)
                viewHolder.parentConstraintLayout.animateHorizontalShake(
                    offset = 50f,
                    repeatCount = 4,
                    duration = 1000L
                )
                if (viewHolder.approvalCheckBox.isChecked) {
                    viewHolder.materialCardView.strokeWidth = 3
                    viewHolder.materialCardView.strokeColor = context.getResources().getColor(R.color.custom_green)
                } else {
                    viewHolder.materialCardView.strokeWidth = 3
                    viewHolder.materialCardView.strokeColor = context.getResources().getColor(R.color.white)
                }
            }
            viewHolder.materialCardView.setOnClickListener {
                plantMoreDetailsCallback(approvalDocumentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return approvalList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

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
        val parentConstraintLayout: ConstraintLayout = itemView.findViewById(R.id.approvalRecyclerViewLayoutParent)
        val materialCardView: MaterialCardView = itemView.findViewById(R.id.materialCardView1)
        val approvalCheckBox: CheckBox = itemView.findViewById(R.id.approvalCheckBox)
        val roundImage: RoundedImageView = itemView.findViewById(R.id.approvalRecyclerviewRoundedImage1)
        val nameTextView: TextView = itemView.findViewById(R.id.approvalRecyclerViewNameTextView1)
        val descriptionTextView: TextView = itemView.findViewById(R.id.approvalRecyclerViewDescriptionTextView1)
        val priceTextView: TextView = itemView.findViewById(R.id.approvalRecyclerViewPriceTextView1)
    }
}