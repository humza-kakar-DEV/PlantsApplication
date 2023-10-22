package com.example.plantsservicefyp.fragment.buyer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.RecyclerViewChatSystemAdapter
import com.example.plantsservicefyp.databinding.FragmentChatBinding
import com.example.plantsservicefyp.model.ChatSystemData
import com.example.plantsservicefyp.model.firebase.Message
import com.example.plantsservicefyp.model.firebase.User
import com.example.plantsservicefyp.util.CurrentUserType
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.constant.UserType
import com.example.plantsservicefyp.util.hideKeyboard
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private lateinit var recyclerViewChatSystemAdapter: RecyclerViewChatSystemAdapter

    private val chatDataList = mutableListOf<ChatSystemData>()

    private lateinit var messageInput: String

    private val chatViewModel: ChatViewModel by viewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private var currentUser: User? = null

    private var currentUserType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

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

        chatViewModel.getAllMessages()
        authenticationViewModel.currentUser()

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Admin -> {
                    currentUser = it.user
                    currentUserType = UserType.Admin.type
                }

                is CurrentUserType.Buyer -> {
                    currentUser = it.user
                    currentUserType = UserType.Buyer.type
                }

                is CurrentUserType.Seller -> {
                    currentUser = it.user
                    currentUserType = UserType.Seller.type
                }

                is CurrentUserType.Exception -> {}
                CurrentUserType.Loading -> {}
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                lifecycleScope.launch {
                    chatViewModel.observeWriteMessage.collect() {
                        when (it) {
                            UiState.Loading -> {
                                requireContext().log("loading")
                            }

                            is UiState.Success -> {
                                requireContext().log("successes: ${it.data}")
                            }

                            is UiState.Exception -> {
                                requireContext().log("exception: ${it.message}")
                            }
                        }
                    }
                }
                lifecycleScope.launch {
                    chatViewModel.observeGetAllMessages.collect() {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().log("message list exception: ${it.message}")
                            }

                            UiState.Loading -> {
                                requireContext().log("message list loading")
                            }

                            is UiState.Success -> {
                                val chatSystemDataList = mutableListOf<ChatSystemData>()
                                it.data?.sortedBy {
                                    it.createdAt
                                }?.forEach {
                                    when (it.messageType) {
                                        UserType.Admin.type -> {
                                            chatSystemDataList.add(
                                                ChatSystemData.AdminChatSystem(
                                                    email = it.email,
                                                    message = it.message
                                                )
                                            )
                                        }

                                        UserType.Buyer.type -> {
                                            chatSystemDataList.add(
                                                ChatSystemData.UserChatSystem(
                                                    email = it.email,
                                                    message = it.message
                                                )
                                            )
                                        }

                                        else -> {}
                                    }
                                }
                                recyclerViewChatSystemAdapter.items = chatSystemDataList
                                binding.chatRecyclerView.smoothScrollToPosition(recyclerViewChatSystemAdapter.items.size - 1)
                            }
                        }
                    }
                }
            }
        }

//!     Get message in this on click listener
        binding.sendButton.setOnClickListener {
            if (!validateMessage()) {
                return@setOnClickListener
            }
            it.hideKeyboard()
            binding.messageEditText.editText?.setText("")
            currentUser?.let { a ->
                currentUserType?.let { b ->
                    chatViewModel.writeMessage(
                        Message(
                            email = a.email,
                            message = messageInput,
                            messageType = b,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                }
            }
        }

        return binding.root
    }

    private fun validateMessage(): Boolean {
        messageInput = binding.messageEditText.editText?.text.toString().trim()
        return if (messageInput.isEmpty()) {
            binding.messageEditText.setError("Field can't be empty")
            false
        } else {
            binding.messageEditText.setError(null)
            true
        }
    }
}