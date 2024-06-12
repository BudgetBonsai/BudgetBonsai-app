package com.example.budgetbonsai.data.remote.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataIstem(

	@field:SerializedName("date")
	val date: Date? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class Date(

	@field:SerializedName("seconds")
	val seconds: Int? = null,

	@field:SerializedName("nanoseconds")
	val nanoseconds: Int? = null
)
