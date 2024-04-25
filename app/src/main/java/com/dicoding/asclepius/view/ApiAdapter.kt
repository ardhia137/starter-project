package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemRowApiBinding

class ApiAdapter : ListAdapter<ArticlesItem, ApiAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val users = getItem(position)
        holder.bind(users)
        holder.itemView.setOnClickListener {
            val websiteUri = Uri.parse(users.url)
            val intent = Intent(Intent.ACTION_VIEW, websiteUri)
            holder.itemView.context.startActivity(intent)
        }
    }
    class MyViewHolder(val binding: ItemRowApiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem){
            binding.tvItemTitle.text = article.title
            binding.tvItemDescription.text = article.description
            Glide.with(binding.root.context)
                .load(article.urlToImage)
                .into(binding.imgItemPhoto)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}