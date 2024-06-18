package com.example.budgetbonsai.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteResponse(

	@field:SerializedName("data")
	val data: DeleteData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DeleteData(

	@field:SerializedName("id")
	val id: String? = null
)
