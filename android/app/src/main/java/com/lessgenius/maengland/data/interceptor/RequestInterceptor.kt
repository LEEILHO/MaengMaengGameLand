package com.lessgenius.maengland.data.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

private const val TAG = "RequestInterceptor_김진영"
class RequestInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

//        Log.d(TAG, "intercept RequestInterceptor: ${sharedPreferences.getString(X_ACCESS_TOKEN)}")
//        try {
//            sharedPreferences.getString(X_ACCESS_TOKEN).let { token ->
//                token?.let {
//                    builder.addHeader("Authorization", "Bearer $token")
//                    Log.d(TAG, "intercept: JWT AccessToken 헤더에 담았습니다.")
//                    return chain.proceed(builder.build())
//                }
//            }
//        } catch (e: Exception) {
//            return chain.proceed(chain.request())
//        }
        return chain.proceed(chain.request())
    }

}