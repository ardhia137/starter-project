package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.databinding.ItemRowApiBinding
import com.dicoding.asclepius.helper.HistoryDiffCallback

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val listHistory = ArrayList<History>()
    fun setListHistory(listHistory: List<History>) {
        val diffCallback = HistoryDiffCallback(this.listHistory, listHistory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listHistory.clear()
        this.listHistory.addAll(listHistory)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemRowApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val users = listHistory[position]
        holder.bind(listHistory[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ResultActivity::class.java)
            intent.putExtra(ResultActivity.LABEL,users.category)
            intent.putExtra(ResultActivity.SCORE,users.score)
            val imageUri = Uri.parse(users.image)
            intent.putExtra(ResultActivity.IMAGE, imageUri)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    inner class HistoryViewHolder(private val binding: ItemRowApiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users: History) {
            with(binding) {
                binding.tvItemTitle.text = users.category
                val scoreInPercent = (users.score!! * 100).toInt()
                binding.tvItemDescription.text = "Score : $scoreInPercent%"
                Glide.with(binding.root.context)
                    .load(users.image)
                    .into(binding.imgItemPhoto)
            }
        }
    }
}