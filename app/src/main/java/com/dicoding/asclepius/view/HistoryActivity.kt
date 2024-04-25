package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.helper.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val historyViewModel = obtainViewModel(this)
        historyViewModel.getAllHistorys().observe(this) { historyList ->
            if (historyList != null) {
                adapter.setListHistory(historyList)
            }
        }
        historyViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        adapter = HistoryAdapter()
        binding?.listApi?.layoutManager = LinearLayoutManager(this)
        binding?.listApi?.setHasFixedSize(true)
        binding?.listApi?.adapter = adapter
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}