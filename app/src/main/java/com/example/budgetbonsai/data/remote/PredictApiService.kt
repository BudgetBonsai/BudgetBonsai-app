package com.example.budgetbonsai.data.remote


import com.example.budgetbonsai.data.remote.response.PredictResponse2
import com.example.budgetbonsai.data.remote.response.WishlistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PredictApiService {
    @GET("predict")
    suspend fun getPredict(): PredictResponse2
}