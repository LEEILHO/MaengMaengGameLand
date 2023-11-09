package com.lessgenius.maengland.data.datasource.remote

import android.util.Log
import com.lessgenius.maengland.data.model.NetworkResult

private const val TAG = "HandleApi_김진영"

// api try catch 문
internal inline fun <T> handleApi(transform: () -> T): NetworkResult<T> = try {
    NetworkResult.Success(transform.invoke())
} catch (e: Exception) {
    when (e) {
        else -> e.message?.let {
            Log.d("TEST", "handleApi: ${e.message}")
            NetworkResult.Error(it)
        } ?: NetworkResult.Error("UnKnown Error Occured")
    }
}