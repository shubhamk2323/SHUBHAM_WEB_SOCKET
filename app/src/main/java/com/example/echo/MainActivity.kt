package com.example.echo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: WebSocketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebSocketScreen1(viewModel)
        }
    }
}

@Composable
fun WebSocketScreen1(viewModel: WebSocketViewModel = viewModel()) {
    val receivedMessage by viewModel.receivedMessages.collectAsState()
    var textInput by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "WebSocket", style = MaterialTheme.typography.headlineSmall)



        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Received: $receivedMessage", style = MaterialTheme.typography.bodyLarge)



        Spacer(modifier = Modifier.height(16.dp))



        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )



        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val message = textInput.text
                if (message.isNotBlank()) {
                    viewModel.sendMessage(message)
                    textInput = TextFieldValue("")
                }
            }
        ) {
            Text("Send Message")
        }

    }
}

