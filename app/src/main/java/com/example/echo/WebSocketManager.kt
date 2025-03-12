package com.example.echo

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration.Companion.seconds

class WebSocketManager {

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }
    private var session: DefaultClientWebSocketSession? = null
    private val _receivedMessages = MutableStateFlow("Not Connected")
    val receivedMessages = _receivedMessages.asStateFlow()

    suspend fun connect() {
        try {
            println("Connecting to WebSocket...") // Debug Log
            client.webSocket("wss://ws.postman-echo.com/raw") {
                session = this
                _receivedMessages.value = "Connected to WebSocket"
                println("WebSocket Connected!") // Debug Log
                receiveMessages()
            }
        } catch (e: Exception) {
            println("Connection failed: ${e.message}")
            _receivedMessages.value = "Connection failed: ${e.message}"
            retryConnection()
        }
    }

    private suspend fun receiveMessages() {
        try {
            println("Waiting for messages...") // Debug Log
            for (frame in session!!.incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    println("Received from hx: $receivedText") // Debug Log
                    _receivedMessages.value = receivedText
                }
            }
        }  catch (e: Exception) {
            println("Error receiving message hx: ${e.message}")
        }
    }



    private suspend fun retryConnection() {
        delay(3.seconds) // Retry after 3 sec
        connect()
    }

    suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

//    suspend fun closeConnection() {
//        session?.close()
//        _receivedMessages.value = "WebSocket closed"
//    }
}
