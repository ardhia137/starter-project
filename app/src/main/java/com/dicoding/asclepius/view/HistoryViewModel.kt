package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.data.repository.HistoryRepository

class HistoryViewModel (application: Application) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)
    fun getHistorys(): LiveData<List<History>> {
        _isLoading.value = true
        var  repo = mHistoryRepository.getHistory()
        _isLoading.value = false
        return repo
    }
    fun getAllHistorys(): LiveData<List<History>> {
        _isLoading.value = true
        var  repo = mHistoryRepository.getAllHistory()
        _isLoading.value = false
        return repo
    }
    fun insert(history: History) {
        mHistoryRepository.insert(history)
    }

}
