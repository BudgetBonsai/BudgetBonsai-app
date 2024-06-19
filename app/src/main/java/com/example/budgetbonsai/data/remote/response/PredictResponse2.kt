package com.example.budgetbonsai.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse2(

	@field:SerializedName("growth_message")
	val growthMessage: String? = null,

	@field:SerializedName("prediction")
	val prediction: Double? = null,

	@SerializedName("monthly_growth") val monthlyGrowth: Map<String, Double>
)

data class MonthlyGrowth(

	@field:SerializedName("4")
	val jsonMember4: Any? = null,

	@field:SerializedName("5")
	val jsonMember5: Any? = null,

	@field:SerializedName("6")
	val jsonMember6: Any? = null
)
