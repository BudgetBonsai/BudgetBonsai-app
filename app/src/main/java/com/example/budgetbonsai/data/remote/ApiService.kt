package com.example.budgetbonsai.data.remote

import com.example.budgetbonsai.data.model.Transaction
import com.example.budgetbonsai.data.remote.response.AddTransactionResponse
import com.example.budgetbonsai.data.remote.response.GetTransactionResponse
import com.example.budgetbonsai.data.remote.response.LoginResponse
import com.example.budgetbonsai.data.remote.response.RegisterResponse
import com.example.budgetbonsai.data.remote.response.WishlistResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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

    @POST("transaction")
    fun addTransaction(@Body transaction: Transaction): Call<AddTransactionResponse>

    @GET("transaction")
    suspend fun getTransactions(): GetTransactionResponse

    @GET("wishlist")
    suspend fun getWishlist(): WishlistResponse

}