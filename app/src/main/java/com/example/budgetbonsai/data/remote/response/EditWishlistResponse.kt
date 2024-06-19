package com.example.budgetbonsai.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditWishlistResponse(

	@field:SerializedName("data")
	val data: EditData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class EditData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("saving_plan")
	val savingPlan: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
