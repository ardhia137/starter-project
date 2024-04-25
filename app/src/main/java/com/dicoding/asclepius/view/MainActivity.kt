package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yalantis.ucrop.UCrop
import java.io.File
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var label: String? = null
    private var score: Float? = null

    private lateinit var adapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage()}
        binding.history.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        val mainViewModel = obtainViewModel(this)
        mainViewModel.getHistorys().observe(this) { historyList ->
            if (historyList != null) {
                adapter.setListHistory(historyList)
            }
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        adapter = HistoryAdapter()
        binding?.listApi?.layoutManager = LinearLayoutManager(this)
        binding?.listApi?.setHasFixedSize(true)
        binding?.listApi?.adapter = adapter

    }

    private fun startGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcherGallery.launch(pickIntent)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                currentImageUri = uri
                val timeStamp = System.currentTimeMillis()
                val destinationFileName = "cropped_image_$timeStamp.jpg"
                val destinationUri = Uri.fromFile(File(cacheDir, destinationFileName))
                val options = UCrop.Options().apply {
                    setCompressionFormat(Bitmap.CompressFormat.JPEG)
                    setCompressionQuality(80)
                    setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.blue))
                    setToolbarWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                    setActiveControlsWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.blue))
                }
                UCrop.of(uri, destinationUri)
                    .withOptions(options)
                    .start(this)
            } ?: Log.e("MainActivity", "Image selection failed")
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            resultUri?.let {
                currentImageUri = resultUri
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.let {
                Log.e("UCrop", "UCrop error: $cropError")
                Toast.makeText(this, "Crop error: $cropError", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                binding.previewImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("MainActivity", "Error loading image from URI: $e")
            }
        }
    }


    private fun analyzeImage() {
        if (currentImageUri == null) {
            showToast("Silakan Masukan Gambar")
            return
        }
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        currentImageUri?.let { uri ->
            val imageClassifierHelper = ImageClassifierHelper(

                context = applicationContext,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        // Tangani kesalahan jika terjadi saat melakukan analisis gambar
                        Log.e("ImageClassifier", "Error: $error")
                    }

                    override fun onResults(
                        results: List<Classifications>?,
                        inferenceTime: Long
                    ) {
                        // Proses hasil klasifikasi di sini
                        results?.let { classifications ->
                            for (classification in classifications) {
                                label = classification.categories[0].label
                                score = classification.categories[0].score
                                Log.d("ImageClassifier", "Label: ${classification.categories[0].label}, Score: ${classification.categories[0].score  * 100}%")
                            }
                        }
                        Log.d("ImageClassifier", "Inference time: $inferenceTime ms")
                    }
                }
            )
            imageClassifierHelper.classifyImage(uri)
            moveToResult()
        }
    }

    private fun moveToResult() {
        historyViewModel = obtainViewModel(this@MainActivity)
        val history = History()
        history.category = label
        history.score = score
        history.image = currentImageUri.toString()
        historyViewModel.insert(history)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.LABEL,label)
        intent.putExtra(ResultActivity.SCORE,score)
        intent.putExtra(ResultActivity.IMAGE,currentImageUri)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }
}