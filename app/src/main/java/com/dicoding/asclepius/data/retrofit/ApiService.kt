package com.dicoding.asclepius.data.retrofit
import com.dicoding.asclepius.data.response.ApiResponse
import retrofit2.Call
import retrofit2.http.*
interface ApiService {

    @GET("v2/top-headlines")
    fun getData(
        @Query("q") q: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("pageSize") pageSize: Int,
    ): Call<ApiResponse>
}