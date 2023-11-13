package com.lessgenius.maengland.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class BestScore(
    @SerializedName("score")
    val score: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable