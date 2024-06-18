package com.example.budgetbonsai.data.model

import java.io.File

data class Wishlist (
    val name: String,
    val amount: String,
    val saving_plan: String,
    val type: String,
    val file: File
)