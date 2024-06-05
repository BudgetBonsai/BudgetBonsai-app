package com.example.budgetbonsai.data.remote

import com.example.budgetbonsai.data.model.Transaction
import com.example.budgetbonsai.data.remote.response.AddTransactionResponse
import com.example.budgetbonsai.data.remote.response.LoginResponse
import com.example.budgetbonsai.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login/email")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("expense")
    fun addTransaction(@Body transaction: Transaction): Call<AddTransactionResponse>

//    @Multipart
//    @POST("stories")
//    fun addTransaction(
//        @Part("date") date : RequestBody,
//        @Part("name") name: RequestBody,
//        @Part("amount") amount: RequestBody,
//        @Part("category") category: RequestBody
//    ): AddTransactionResponse
}