package com.lessgenius.maengland.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class User(
    @SerializedName("lose")
    val lose: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profile")
    val profile: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("tier")
    val tier: String,
    @SerializedName("userEmail")
    val userEmail: String,
    @SerializedName("win")
    val win: Int
) : Parcelable