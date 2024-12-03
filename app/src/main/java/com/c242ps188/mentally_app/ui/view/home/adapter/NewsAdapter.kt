package com.c242ps188.mentally_app.ui.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.C242PS188.mentally_app.databinding.ListArticlesBinding
import com.bumptech.glide.Glide
import com.c242ps188.mentally_app.data.local.model.News

class NewsAdapter(private val onClick: (News) -> Unit)
    : ListAdapter<News, NewsAdapter.ViewHolder>(DIFF_UTILS) {

    class ViewHolder(private val binding: ListArticlesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: News, onClick: (News) -> Unit){
            Glide.with(binding.root.context)
                .load(item.photo)
                .into(binding.itemImage)

            binding.tvTitleArticle.text = item.title

            binding.root.setOnClickListener { onClick(item) }
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