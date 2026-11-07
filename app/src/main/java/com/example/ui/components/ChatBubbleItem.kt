package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssistantEmotion
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.ui.theme.MayaCyanAccent
import com.example.ui.theme.MayaPinkPrimary
import com.example.ui.theme.MayaPinkSecondary
import com.example.ui.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatBubbleItem(
    message: ChatMessage,
    assistantName: String,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUser = message.sender == MessageSender.USER
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    val timeStr = timeFormat.format(Date(message.timestamp))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(if (isUser) "user_message_${message.id}" else "assistant_message_${message.id}"),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // Assistant Avatar on left
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(MayaPinkPrimary, MayaPinkSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = assistantName.take(1),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Message Content Box
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            val bubbleShape = if (isUser) {
                RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)
            } else {
                RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
            }

            val bubbleBackground = if (isUser) {
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6A1B9A),
                        Color(0xFF4A148C)
                    )
                )
            } else {
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF26123D),
                        Color(0xFF1B0B2E)
                    )
                )
            }

            Surface(
                shape = bubbleShape,
                modifier = Modifier
                    .clip(bubbleShape)
                    .background(bubbleBackground)
                    .border(
                        1.dp,
                        if (isUser) Color(0x44CE93D8) else Color(0x33FF3377),
                        bubbleShape
                    ),
                color = Color.Transparent
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Header tag for Assistant (Emotion or Category)
                    if (!isUser) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = assistantName,
                                color = MayaPinkSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val emotionIcon = when (message.emotion) {
                                AssistantEmotion.LOVE -> "💖"
                                AssistantEmotion.HAPPY -> "😊"
                                AssistantEmotion.CARING -> "🌸"
                                AssistantEmotion.POETIC -> "✨"
                                AssistantEmotion.WITTY -> "😄"
                                AssistantEmotion.SMART -> "⚡"
                                AssistantEmotion.GREETING -> "👋"
                            }
                            Text(text = emotionIcon, fontSize = 11.sp)
                        }
                    }

                    // Main Text (Hindi script preserved cleanly with pleasant line spacing)
                    Text(
                        text = message.text,
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Bottom Row: Timestamp and Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = timeStr,
                            color = TextMuted,
                            fontSize = 10.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Copy button
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(message.text))
                                    Toast.makeText(context, "कॉपी किया गया!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "कॉपी करें",
                                    tint = TextMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                            }

                            // Speak aloud button for assistant replies
                            if (!isUser) {
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = onSpeakClick,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "आवाज़ सुनें",
                                        tint = MayaPinkSecondary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
