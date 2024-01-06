package com.appjam.miracle.feature.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appjam.miracle.local.MiracleDataBase
import com.appjam.miracle.remote.RetrofitBuilder
import com.appjam.miracle.remote.request.MessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class ChatViewModel: ViewModel() {

    private val _sideEffect = Channel<ChatSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _itemState = MutableStateFlow(ChatState())
    val itemState = _itemState.asStateFlow()

    fun loadFirst(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            MiracleDataBase.getInstance(context)!!.drawDao().getMember(0)
        }.onSuccess {
            val data =  _itemState.value.items.toMutableList()
            data.add(
                ChatRvItem(
                    type = ChatRvType.First,
                    content = it.message,
                    image = it.image
                )
            )
            _itemState.value = _itemState.value.copy(
                items = data
            )
        }.onFailure {
            _sideEffect.send(ChatSideEffect.Failed(it))
        }
    }

    fun sendMessage(
        message: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        val data =  _itemState.value.items.toMutableList()
        data.add(
            ChatRvItem(
                type = ChatRvType.My,
                content = message
            )
        )
        data.add(
            ChatRvItem(
                type = ChatRvType.Loading,
                content = ""
            )
        )
        _itemState.value = _itemState.value.copy(
            items = data
        )
        kotlin.runCatching {
            RetrofitBuilder.getChatGptService().postMessage(
                data = MessageRequest(message)
            )
        }.onSuccess {
            Log.d("TAG", "sendMessage: Sucess")
            val newData = _itemState.value.items.toMutableList()
            newData.removeLast()
            newData.add(
                ChatRvItem(
                    type = ChatRvType.Your,
                    content = it.message
                )
            )
            _itemState.value = _itemState.value.copy(
                items = newData
            )
            _sideEffect.send(ChatSideEffect.Success)
        }.onFailure {
            Log.d("TAG", "sendMessage: Failed")
            Log.d("TAG", "sendMessage: run")
            val newData = _itemState.value.items.toMutableList()
            newData.removeLast()
            _itemState.value = _itemState.value.copy(
                items = newData
            )
            try {
                _sideEffect.send(ChatSideEffect.Failed(it))
                Log.d("TAG", "sendMessage: run2")
            } catch (e: Exception) {
                Log.d("TAG", "sendMessage: ${e.message}")
            }
        }
    }
}