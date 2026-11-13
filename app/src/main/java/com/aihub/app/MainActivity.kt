package com.aihub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class AiModel(val title: String, val subtitle: String) {
    CHATGPT("ChatGPT", "OpenAI"),
    DEEPSEEK("DeepSeek", "DeepSeek"),
    CLAUDE("Claude", "Anthropic")
}

private data class ChatMessage(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF9B8CFF),
                    background = Color(0xFF0B0D12),
                    surface = Color(0xFF171A22),
                    onBackground = Color(0xFFF4F4F7),
                    onSurface = Color(0xFFF4F4F7)
                )
            ) {
                AiHubScreen()
            }
        }
    }
}

@Composable
private fun AiHubScreen() {
    var selectedModel by remember { mutableStateOf(AiModel.CHATGPT) }
    var input by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "Привет! Это AI HUB. Выбери модель выше и напиши сообщение. Пока работает демонстрационный интерфейс — подключение API будет следующим этапом.",
                false
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0B0D12)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("AI HUB", fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text("Три модели. Один чат.", color = Color(0xFF9298A8), fontSize = 13.sp)
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFF252239), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AI", color = Color(0xFFB8ADFF), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(22.dp))
            Text("ВЫБЕРИ МОДЕЛЬ", color = Color(0xFF9298A8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(9.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                AiModel.entries.forEach { model ->
                    val isSelected = selectedModel == model
                    Button(
                        onClick = { selectedModel = model },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF7565D9) else Color(0xFF1A1D27),
                            contentColor = Color.White
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 5.dp, vertical = 11.dp)
                    ) {
                        Text(model.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF171A22)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(selectedModel.title, fontWeight = FontWeight.SemiBold)
                        Text(selectedModel.subtitle, color = Color(0xFF9298A8), fontSize = 12.sp)
                    }
                    Text("ДЕМО", color = Color(0xFFB8ADFF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("ПЕРЕПИСКА", color = Color(0xFF9298A8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF171A22)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.weight(1f).padding(vertical = 9.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 14.sp),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Color(0xFFB8ADFF)),
                        decorationBox = { inner ->
                            Box {
                                if (input.isEmpty()) {
                                    Text("Напиши сообщение...", color = Color(0xFF777E8E), fontSize = 14.sp)
                                }
                                inner()
                            }
                        }
                    )
                    Button(
                        onClick = {
                            val text = input.trim()
                            if (text.isNotEmpty()) {
                                messages.add(ChatMessage(text, true))
                                messages.add(
                                    ChatMessage(
                                        "Сообщение подготовлено для ${selectedModel.title}. На следующем этапе подключим API, чтобы получать настоящий ответ модели.",
                                        false
                                    )
                                )
                                input = ""
                            }
                        },
                        enabled = input.isNotBlank(),
                        shape = CircleShape,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Отправить")
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "AI HUB 0.1.0 • интерфейс-предпросмотр",
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF626979),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(if (message.fromUser) 0.86f else 0.94f)
                .background(
                    color = if (message.fromUser) Color(0xFF302A50) else Color(0xFF171A22),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(13.dp)
        ) {
            Text(message.text, color = Color(0xFFF0F0F5), fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}
