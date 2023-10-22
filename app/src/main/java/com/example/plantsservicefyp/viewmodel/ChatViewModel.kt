package com.example.plantsservicefyp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsservicefyp.model.firebase.Message
import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.util.UiState
import com.example.plantsservicefyp.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.engine.callContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _observeWriteMessage = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val observeWriteMessage: StateFlow<UiState<Boolean>>
        get() = _observeWriteMessage

    private val _observeGetAllMessages = MutableStateFlow<UiState<List<Message>>>(UiState.Loading)
    val observeGetAllMessages: StateFlow<UiState<List<Message>>>
        get() = _observeGetAllMessages

    fun writeMessage(message: Message) {
        plantRepository
            .writeMessage(message)
            .onStart {
                _observeWriteMessage.value = UiState.Loading
            }
            .catch {
                _observeWriteMessage.value = UiState.Exception(it.message)
            }
            .onCompletion {
                if (it != null) {
//!                 exception occurred
                    _observeWriteMessage.value = UiState.Success(false)
                } else {
//!                 code executed perfectly
                    _observeWriteMessage.value = UiState.Success(true)
                }
            }
            .launchIn(viewModelScope)
    }

    fun getAllMessages() {
        plantRepository
            .getAllMessages()
            .onStart {
                _observeGetAllMessages.value = UiState.Loading
            }
            .onEach {
                _observeGetAllMessages.value = UiState.Success(it)
            }
            .catch {
                _observeGetAllMessages.value = UiState.Exception(it.message)
            }
            .launchIn(viewModelScope)
    }

}