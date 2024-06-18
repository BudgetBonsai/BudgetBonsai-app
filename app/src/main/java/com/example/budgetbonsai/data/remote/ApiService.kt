package com.example.budgetbonsai.data.remote

import com.example.budgetbonsai.data.model.Transaction
import com.example.budgetbonsai.data.model.Wishlist
import com.example.budgetbonsai.data.remote.response.AddTransactionResponse
import com.example.budgetbonsai.data.remote.response.AddWishlistResponse
import com.example.budgetbonsai.data.remote.response.GetTransactionResponse
import com.example.budgetbonsai.data.remote.response.LoginResponse
import com.example.budgetbonsai.data.remote.response.RegisterResponse
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.data.remote.response.WishlistResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @POST("transaction")
    fun addTransaction(@Body transaction: Transaction): Call<AddTransactionResponse>

    @GET("transaction")
    suspend fun getTransactions(): GetTransactionResponse

    @GET("wishlist")
    suspend fun getWishlist(): WishlistResponse

//    @FormUrlEncoded
//    @POST("wishlist")
//    suspend fun addWishlist(
//        @Field("name") name: String,
//        @Field("amount") amount: Int,
//        @Field("saving_plan") savingPlan: String,
//        @Field("type") type: String,
//        @Field("file") file: String
//    ): AddWishlistResponse

    @Multipart
    @POST("wishlist")
    suspend fun addWishlist(
        @Part("name") name: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("saving_plan") savingPlan: RequestBody,
        @Part("type") type: RequestBody,
        @Part file: MultipartBody.Part
    ): AddWishlistResponse

}