package com.example.budgetbonsai.data.remote

import android.util.Log
import com.example.budgetbonsai.data.local.UserPreference
import com.github.mikephil.charting.BuildConfig
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PredictApiConfig {
    fun getApiService(userPreference: UserPreference): PredictApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val token = runBlocking {
                val tokenValue = userPreference.getSession().firstOrNull()?.token ?: ""
                tokenValue
            }
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Timeout saat mencoba melakukan koneksi ke server
            .readTimeout(30, TimeUnit.SECONDS)    // Timeout saat membaca respons dari server
            .writeTimeout(30, TimeUnit.SECONDS)   // Timeout saat menulis ke server
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://test-flask-mpyzges47a-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PredictApiService::class.java)
    }
}