package com.dicoding.asclepius.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.response.ApiResponse
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.retrofit.ApiConfig

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultViewModel : ViewModel() {

    private val _article = MutableLiveData<List<ArticlesItem>>()
    val article: LiveData<List<ArticlesItem>> = _article
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    companion object{
        private const val TAG = "ResultViewModel"
    }

    init {
        getData()
    }
    fun getData() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getData(
            "cancer","health","en",5
        )
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _article.value = responseBody.articles as List<ArticlesItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure response: ${response}")
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure aja: ${t.message}")
            }
        })
    }
}