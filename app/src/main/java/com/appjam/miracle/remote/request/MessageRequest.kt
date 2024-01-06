package com.appjam.miracle.remote.request

import com.google.gson.annotations.SerializedName

data class MessageRequest(
    @field:SerializedName("message")
    val message: String
)