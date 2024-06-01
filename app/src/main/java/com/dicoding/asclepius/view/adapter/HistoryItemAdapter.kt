package com.dicoding.asclepius.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryItemAdapter(
    context: Context
): ListAdapter<HistoryEntity, HistoryItemAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemHistoryBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(resultItem: HistoryEntity) {
            with(binding) {
                tvStatusResult.text = resultItem.status
                tvStatusPercentage.text = resultItem.percentage
                tvTime.text = resultItem.dateTime
                Glide.with(imgItemPhoto)
                    .load(resultItem.photo)
                    .placeholder(R.drawable.empty_image)
                    .error(R.drawable.empty_image)
                    .into(binding.imgItemPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}