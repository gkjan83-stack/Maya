package com.example.data.model

import java.util.UUID

enum class MessageSender {
    USER,
    ASSISTANT
}

enum class AssistantEmotion {
    LOVE,
    HAPPY,
    CARING,
    WITTY,
    SMART,
    GREETING,
    POETIC
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val emotion: AssistantEmotion = AssistantEmotion.HAPPY,
    val isSpoken: Boolean = false
)
