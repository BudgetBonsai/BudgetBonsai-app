package com.example.budgetbonsai.data.remote.response

import com.google.gson.annotations.SerializedName

data class aPredictResponse(
	@SerializedName("growth_message") val growthMessage: String,
	@SerializedName("monthly_growth") val monthlyGrowth: Map<String, Double>,
	@SerializedName("prediction") val prediction: Double
)