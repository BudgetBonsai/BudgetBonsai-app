package com.example.budgetbonsai.data.model

data class Transaction(
    val date: String,
    val name: String,
    val amount: Int,
    val category: String,
    val type: String
)
