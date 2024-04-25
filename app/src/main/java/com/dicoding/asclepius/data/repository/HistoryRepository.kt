package com.dicoding.asclepius.data.repository
import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.data.database.HistoryDao
import com.dicoding.asclepius.data.database.HistoryRoomDatabase

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository (application: Application) {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryRoomDatabase.getDatabase(application)
        mHistoryDao = db.historyDao()
    }

    fun getHistory(): LiveData<List<History>> = mHistoryDao.getHistorys()
    fun getAllHistory(): LiveData<List<History>> = mHistoryDao.getAllHistorys()
      fun insert(history: History) {
        executorService.execute { mHistoryDao.insert(history) }
    }



}