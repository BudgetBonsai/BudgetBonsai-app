package com.example.budgetbonsai.data.model

data class DepositRequest(
    val date: String,
    val amount: Int,
    val type: String
)