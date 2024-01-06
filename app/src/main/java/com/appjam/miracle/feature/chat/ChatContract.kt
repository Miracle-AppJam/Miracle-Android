package com.appjam.miracle.feature.chat

import android.graphics.Bitmap

data class ChatState(
    val items: List<ChatRvItem> = emptyList()
)

data class ChatRvItem(
    val type: ChatRvType,
    val content: String,
    val image: Bitmap? = null
)

sealed class ChatRvType {
    object First: ChatRvType()
    object My: ChatRvType()
    object Your: ChatRvType()
    object Loading: ChatRvType()
}

sealed class ChatSideEffect {
    object Success: ChatSideEffect()
    data class Failed(val throwable: Throwable): ChatSideEffect()
}