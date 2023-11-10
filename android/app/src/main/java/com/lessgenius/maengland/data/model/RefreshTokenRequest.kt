package com.lessgenius.maengland.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
) : Parcelable