package com.c242ps188.mentally_app.ui.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.C242PS188.mentally_app.databinding.ListArticlesBinding
import com.c242ps188.mentally_app.data.local.model.News

class NewsAdapter(private val onClick: (News) -> Unit)
    : ListAdapter<News, NewsAdapter.ViewHolder>(DIFF_UTILS) {

    class ViewHolder(private val binding: ListArticlesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: News, onClick: (News) -> Unit){
            binding.itemImage.setImageResource(item.photo)
            binding.tvTitleArticle.setText(item.title)
            if (item.id == 1) {
                val layoutParams = binding.container.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.marginStart = 46
                binding.container.layoutParams = layoutParams
            }

            binding.container.setOnClickListener {
                onClick(item)
            }
        }

    }

    companion object {
        val DIFF_UTILS = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }
}