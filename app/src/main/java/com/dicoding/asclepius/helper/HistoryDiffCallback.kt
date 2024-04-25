package com.dicoding.asclepius.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.data.database.History

class HistoryDiffCallback  (private val oldHistoryList: List<History>, private val newHistoryList: List<History>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldHistoryList.size
    override fun getNewListSize(): Int = newHistoryList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHistoryList[oldItemPosition].id == newHistoryList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldHistoryList[oldItemPosition]
        val newNote = newHistoryList[newItemPosition]
        return oldNote.image == newNote.image && oldNote.category == newNote.category && oldNote.score == newNote.score
    }
}