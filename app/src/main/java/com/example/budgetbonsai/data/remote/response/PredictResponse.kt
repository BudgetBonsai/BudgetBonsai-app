package com.example.budgetbonsai.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("growth_message")
	val growthMessage: String? = null,

	@field:SerializedName("prediction")
	val prediction: Any? = null,

	@field:SerializedName("monthly_growth")
	val monthlyGrowth: MonthlyGrowth? = null
)

data class MonthlyGrowth(

	@field:SerializedName("4")
	val jsonMember4: Any? = null,

	@field:SerializedName("5")
	val jsonMember5: Any? = null,

	@field:SerializedName("6")
	val jsonMember6: Any? = null
)
