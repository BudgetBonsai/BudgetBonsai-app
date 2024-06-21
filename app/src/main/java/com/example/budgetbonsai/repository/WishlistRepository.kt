package com.example.budgetbonsai.repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetbonsai.data.model.DepositRequest
import com.example.budgetbonsai.data.remote.ApiService
import com.example.budgetbonsai.data.remote.response.AddAmountResponse
import com.example.budgetbonsai.data.remote.response.AddWishlistResponse
import com.example.budgetbonsai.data.remote.response.DeleteResponse
import com.example.budgetbonsai.data.remote.response.EditWishlistResponse
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.data.remote.response.WishlistResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream

class WishlistRepository(private val apiService: ApiService) {

    suspend fun getWishlist(): LiveData<Result<List<WishlistItem>>> {
        val resultLiveData = MutableLiveData<Result<List<WishlistItem>>>()
        try {
            val response = apiService.getWishlist()
            if (response.error == false) {
                val data = response.data?.filterNotNull() ?: emptyList()
                resultLiveData.value = Result.Success(data)
            } else {
                val errorMessage = response.message ?: "Error occurred"
                resultLiveData.value = Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            resultLiveData.value = Result.Error("Network error occurred")
        }
        return resultLiveData
    }

    suspend fun addWishlist(
        name: RequestBody,
        amount: RequestBody,
        savingPlan: RequestBody,
        type: RequestBody,
        file: MultipartBody.Part
    ): AddWishlistResponse {
        return apiService.addWishlist(name, amount, savingPlan, type, file)
    }

    suspend fun editWishlist(
        id: String,
        name: RequestBody,
        amount: RequestBody,
        savingPlan: RequestBody,
        type: RequestBody,
        file: MultipartBody.Part
    ): EditWishlistResponse {
        val response = apiService.editWishlist(id, name, amount, savingPlan, type, file)
        if (!response.error!!) {
            return response
        } else {
            throw HttpException(response.toResponse())
        }
    }

    fun EditWishlistResponse.toResponse(): retrofit2.Response<EditWishlistResponse> {
        return retrofit2.Response.success(this)
    }


    suspend fun deleteWishlist(id: String): Result<DeleteResponse> {
        return try {
            val response = apiService.deleteWishlist(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to delete wishlist: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Failed to delete wishlist")
        }
    }

    suspend fun depositWishlistAmount(id: String, date: String, amount: Int): AddAmountResponse {
        val depositRequest = DepositRequest(date, amount, "Income")
        return apiService.depositWishlistAmount(id, depositRequest)
    }
}