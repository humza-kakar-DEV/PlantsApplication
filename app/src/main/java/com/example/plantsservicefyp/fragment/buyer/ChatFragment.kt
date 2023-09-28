package com.example.plantsservicefyp.fragment.buyer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.RecyclerViewChatSystemAdapter
import com.example.plantsservicefyp.databinding.FragmentChatBinding
import com.example.plantsservicefyp.model.ChatSystemData
import com.example.plantsservicefyp.util.hideKeyboard
import com.example.plantsservicefyp.util.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private lateinit var recyclerViewChatSystemAdapter: RecyclerViewChatSystemAdapter

    private val chatDataList = mutableListOf<ChatSystemData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        chatDataList.add(
            ChatSystemData.UserChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        chatDataList.add(
            ChatSystemData.UserChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        chatDataList.add(
            ChatSystemData.UserChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        chatDataList.add(
            ChatSystemData.UserChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        chatDataList.add(
            ChatSystemData.AdminChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        chatDataList.add(
            ChatSystemData.UserChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        chatDataList.add(
            ChatSystemData.UserChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        chatDataList.add(
            ChatSystemData.AdminChatSystem(
                "bilal@gmail.com",
                "example text jlkjsdkljflkdsj"
            )
        )

        recyclerViewChatSystemAdapter = RecyclerViewChatSystemAdapter(requireContext()) {
            requireContext().log("items clicked!")
        }

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        binding.chatRecyclerView.adapter = recyclerViewChatSystemAdapter

        recyclerViewChatSystemAdapter.items = chatDataList

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

//!     Get message in this on click listener
        binding.sendButton.setOnClickListener {
            it.hideKeyboard()
        }

        return binding.root
    }
}