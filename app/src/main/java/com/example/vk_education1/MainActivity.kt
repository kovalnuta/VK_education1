package com.example.vk_education1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите текст или номер") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (inputText.isBlank()) {
                    Toast.makeText(context, "Поле не может быть пустым", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(context, MainActivity2::class.java)
                    intent.putExtra("EXTRA_TEXT", inputText)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Открыть вторую Activity")
        }

        Button(
            onClick = {
                if (inputText.isBlank()) {
                    Toast.makeText(context, "Поле не может быть пустым", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$inputText")
                    }
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Позвонить другу")
        }

        Button(
            onClick = {
                if (inputText.isBlank()) {
                    Toast.makeText(context, "Поле не может быть пустым", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, inputText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Поделиться через..."))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Поделиться текстом")
        }
    }
}