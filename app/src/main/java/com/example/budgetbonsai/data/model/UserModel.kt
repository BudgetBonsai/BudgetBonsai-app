package com.example.budgetbonsai.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var email: String,
    var token: String,
    var isLogin: Boolean = false
) : Parcelable

//@Entity(tableName = "users")
//data class UserModel(
//    @PrimaryKey val email: String,
//    val name: String,
//    val password: String
//)