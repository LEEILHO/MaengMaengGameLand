package com.lessgenius.maengland.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class RefreshTokenRequest(
    @SerializedName("watchAccessToken")
    val watchAccessToken: String,
    @SerializedName("watchRefreshToken")
    val watchRefreshToken: String
) : Parcelable