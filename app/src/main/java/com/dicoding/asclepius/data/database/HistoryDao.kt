package com.dicoding.asclepius.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: History)
    @Query("SELECT * from history ORDER BY id DESC LIMIT 3")
    fun getHistorys(): LiveData<List<History>>
    @Query("SELECT * from history ORDER BY id DESC")
    fun getAllHistorys(): LiveData<List<History>>
}