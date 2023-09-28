package com.example.plantsservicefyp.fragment.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.PlantsReminderRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentReminderListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentReminderListBinding

    private lateinit var plantsReminderRecyclerViewAdapter: PlantsReminderRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderListBinding.inflate(layoutInflater, container, false)

        plantsReminderRecyclerViewAdapter = PlantsReminderRecyclerViewAdapter(requireContext())

        binding.reminderListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.reminderListRecyclerView.adapter = plantsReminderRecyclerViewAdapter

        return binding.root
    }
}