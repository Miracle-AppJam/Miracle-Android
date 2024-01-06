package com.appjam.miracle.feature.draw

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appjam.miracle.local.DrawEntity
import com.appjam.miracle.local.MiracleDataBase
import com.appjam.miracle.remote.RetrofitBuilder
import com.appjam.miracle.remote.request.MessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.wait
import okio.BufferedSink

class DrawViewModel: ViewModel() {

    private val _drawSideEffect = Channel<DrawSideEffect>()
    val drawSideEffect = _drawSideEffect.receiveAsFlow()

    fun postImage(
        bitmap: Bitmap,
        context: Context
    ) {
        val bitmapRequestBody = BitmapRequestBody(bitmap)

        //multipart/form-data
        val bitmapMultipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("file", "draw.jpeg", bitmapRequestBody)

        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                RetrofitBuilder.getChatGptService().postMessage(
                    image = bitmapMultipartBody,
                    data = MessageRequest("")
                )
            }.onSuccess {
                Log.d("TAG", "postImage: $it")
                kotlin.runCatching {  }
                val dao = MiracleDataBase.getInstance(context)?.drawDao()
                dao?.insertMember(
                    entity = DrawEntity(
                        image = bitmap,
                        message = it.message
                    )
                )
                _drawSideEffect.send(DrawSideEffect.SUCCESS)


            }.onFailure {
                Log.d("TAG", "postImage: $it")
                _drawSideEffect.send(DrawSideEffect.FAILED)
            }
        }
    }

    fun saveImage(
        bitmap: Bitmap,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            MiracleDataBase.getInstance(context)?.drawDao()?.insertMember(
                DrawEntity(
                    image = bitmap,
                    message = ""
                )
            )
        }
    }
}

class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
    override fun contentType(): MediaType? = "image/jpeg".toMediaType()

    override fun writeTo(sink: BufferedSink) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, sink.outputStream())
    }
}

sealed class DrawSideEffect() {
    object SUCCESS: DrawSideEffect()
    object FAILED: DrawSideEffect()
    object LOADING: DrawSideEffect()
}