package com.example.budgetbonsai.ui.wishlist

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.data.remote.response.AddWishlistResponse
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.repository.WishlistRepository
import com.example.budgetbonsai.ui.transaction.TransactionViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class WishlistViewModel(private val wishlistRepository: WishlistRepository, private val context: Context) : ViewModel() {

    private val _wishlistLiveData = MutableLiveData<Result<List<WishlistItem>>>()
    val wishlistLiveData: LiveData<Result<List<WishlistItem>>> = _wishlistLiveData
    private val _addWishlistResult = MutableLiveData<Result<AddWishlistResponse>>()
    val addWishlistResult: LiveData<Result<AddWishlistResponse>> = _addWishlistResult

    var imageUri: Uri? = null

    init {
        fetchWishlist()
    }

    fun fetchWishlist() {
        _wishlistLiveData.value = Result.Loading
        viewModelScope.launch {
            try {
                val result = wishlistRepository.getWishlist()
                _wishlistLiveData.postValue(result.value)
            } catch (e: Exception) {
                _wishlistLiveData.postValue(Result.Error("Network error occurred"))
            }
        }
    }

    fun addWishlist(name: String, amount: Int, savingPlan: String, type: String, fileUri: String) {
        _addWishlistResult.value = Result.Loading
        viewModelScope.launch {
            try {
                val file = uriToFile(Uri.parse(fileUri), context)
                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
                val amountPart = RequestBody.create("text/plain".toMediaTypeOrNull(), amount.toString())
                val savingPlanPart = RequestBody.create("text/plain".toMediaTypeOrNull(), savingPlan)
                val typePart = RequestBody.create("text/plain".toMediaTypeOrNull(), type)

                val result = wishlistRepository.addWishlist(namePart, amountPart, savingPlanPart, typePart, filePart)
                _addWishlistResult.postValue(Result.Success(result))
            } catch (e: Exception) {
                _addWishlistResult.postValue(Result.Error("Failed to add wishlist"))
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return File(filePath)
    }

    class WishlistViewModelFactory(private val repository: WishlistRepository, private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WishlistViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}