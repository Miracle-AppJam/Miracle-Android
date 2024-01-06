package com.appjam.miracle.remote.service

import android.graphics.Bitmap
import com.appjam.miracle.remote.request.MessageRequest
import com.appjam.miracle.remote.response.MessageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ChatGptService {

    @Multipart
    @POST("/chatgpt/data")
    suspend fun postMessage(
        @Part image: MultipartBody.Part,
        @Part("data") data: MessageRequest
    ): MessageResponse


    @POST("/chatgpt/data")
    suspend fun postMessage(
        @Body data: MessageRequest
    ): MessageResponse
}