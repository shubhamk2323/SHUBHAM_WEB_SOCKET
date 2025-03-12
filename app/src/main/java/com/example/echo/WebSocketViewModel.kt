package com.example.echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WebSocketViewModel : ViewModel() {

    private val webSocketManager = WebSocketManager()
    private val _messageFlow = MutableStateFlow("Not Connected")
    val receivedMessages: StateFlow<String> = webSocketManager.receivedMessages
    val messageFlow = _messageFlow.asStateFlow()

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        viewModelScope.launch {
            webSocketManager.connect()
            webSocketManager.receivedMessages.collect { message ->
                _messageFlow.value = message
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            webSocketManager.sendMessage(message)
        }
    }
}
