package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityResultBinding
import java.io.IOException

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val label = intent.getStringExtra(LABEL)
        val score = intent.getFloatExtra(SCORE, 0F)
        val finalScore = score*100
        currentImageUri = intent.getParcelableExtra<Uri>(IMAGE)
        binding.resultText.text = "Category : $label \nScore = ${finalScore.toInt()}%"
        showImage()

        val resultViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ResultViewModel::class.java)
        resultViewModel.article.observe(this) { article ->
            setUsersData(article)
        }
        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.listApi.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listApi.addItemDecoration(itemDecoration)

        resultViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    private fun showImage() {
        currentImageUri?.let { uri ->
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                binding.resultImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("ResultActivity", "Error loading image from URI: $e")
            }
        }
    }
    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
    private fun setUsersData(article: List<ArticlesItem?>?) {
        val adapter = ApiAdapter()
        adapter.submitList(article)
        binding.listApi.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    companion object{
        const val LABEL = "label"
        const val SCORE = "score"
        const val IMAGE = "image"
    }


}