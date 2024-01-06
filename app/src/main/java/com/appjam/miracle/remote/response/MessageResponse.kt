package com.appjam.miracle.remote.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @field:SerializedName("message")
    val message: String
)
r