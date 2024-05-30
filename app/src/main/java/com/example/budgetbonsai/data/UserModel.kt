package com.example.budgetbonsai.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var email: String,
    var token: String,
    var isLogin: Boolean = false
) : Parcelable