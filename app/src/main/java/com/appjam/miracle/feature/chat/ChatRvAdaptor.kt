package com.appjam.miracle.feature.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.appjam.miracle.databinding.ItemMyBinding
import com.appjam.miracle.databinding.ItemYourBinding

class ChatRvAdaptor constructor(
    private val items: List<ChatRvItem>
): RecyclerView.Adapter<ChatRvAdaptor.ViewHolder>() {
    sealed class ViewHolder(
        binding: ViewBinding
    ): RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: ChatRvItem)

        class MultiMyViewHolder(
            private val binding: ItemMyBinding
        ): ViewHolder(binding) {

            override fun bind(item: ChatRvItem) {
                binding.textContent.text = item.content
            }
        }

        class MultiYourViewHolder(
            private val binding: ItemYourBinding
        ): ViewHolder(binding) {

            override fun bind(item: ChatRvItem) {
                if (item.type is ChatRvType.Loading) {
                    binding.layoutContent.visibility = View.GONE
                    binding.lottieLoading.visibility = View.GONE
                } else {
                    if (item.type is ChatRvType.First) {
                        binding.imageThumbnail.visibility = View.VISIBLE
                        binding.imageThumbnail.setImageBitmap(item.image)
                    }
                    binding.textContent.text = item.content
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when(viewType) {
            0 -> {
                ViewHolder.MultiYourViewHolder(
                    ItemYourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> {
                ViewHolder.MultiMyViewHolder(
                    ItemMyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }

    override fun getItemViewType(position: Int): Int = viewType(items[position].type)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        // 채팅 윗 챗이 같은 경우 구별
//        if (position != 0 && items[position-1].author == item[position].author) {
//            holder.bind(context, true, item[position], targetThumbnail)
//        } else {
//            holder.bind(context, false, item[position], targetThumbnail)
//        }
    }

    private fun viewType(type: ChatRvType): Int =
        when(type) {
            is ChatRvType.First -> 0
            is ChatRvType.Your -> 0
            is ChatRvType.Loading -> 0
            is ChatRvType.My -> 1
            else -> 1
        }

}