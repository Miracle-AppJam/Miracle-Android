package com.appjam.miracle.feature.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.appjam.miracle.databinding.FragmentChatBinding
import com.appjam.miracle.utiles.shortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatFragment: Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel.loadFirst(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itemState.collect {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        binding.rvChat.adapter = ChatRvAdaptor(it.items)
                        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffect.collect {
                    when(it) {
                        is ChatSideEffect.Failed -> {
                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                requireContext().shortToast(it.throwable.message.toString())
                                binding.editChat.isEnabled = true
                            }
                        }
                        is ChatSideEffect.Success -> {
                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                binding.editChat.isEnabled = true
                            }
                        }
                    }
                }
            }
        }

        binding.layoutSend.setOnClickListener {
            val chat = binding.editChat.text.toString()
            if (chat.isNullOrBlank() || chat.length < 3) {
                requireContext().shortToast("최소 3자 이상의 글자를 입력해주세요")
                return@setOnClickListener
            }
            binding.editChat.text.clear()
            binding.editChat.isEnabled = false
            viewModel.sendMessage(chat)
        }

        return binding.root
    }
}