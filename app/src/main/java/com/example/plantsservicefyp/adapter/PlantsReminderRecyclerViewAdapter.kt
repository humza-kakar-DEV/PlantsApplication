package com.example.plantsservicefyp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsservicefyp.databinding.ReminderPlantRecyclerViewLayoutBinding

class PlantsReminderRecyclerViewAdapter (
    private val context: Context
): RecyclerView.Adapter<PlantsReminderRecyclerViewAdapter.ViewHolder>() {

    var items = listOf<Int>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: ReminderPlantRecyclerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReminderPlantRecyclerViewLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

        }
    }

}