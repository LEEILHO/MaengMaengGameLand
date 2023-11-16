package com.lessgenius.maengland.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class RequestScore(
    @SerializedName("score")
    val score: Int
) : Parcelable