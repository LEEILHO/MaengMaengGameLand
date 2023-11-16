package com.lessgenius.maengland.data.interceptor

import android.util.Log
import com.lessgenius.maengland.data.datasource.local.PreferencesManager
import com.lessgenius.maengland.data.datasource.local.PreferencesManager.Companion.ACCESS_TOKEN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "RequestInterceptor_김진영"

class RequestInterceptor @Inject constructor(
    private val preferences: PreferencesManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val token = preferences.getTokenSync(ACCESS_TOKEN)
        Log.d(TAG, "intercept RequestInterceptor: ${token}")
        try {
            token.let {
                builder.addHeader("Authorization", "Bearer $token")
                Log.d(TAG, "intercept: JWT AccessToken 헤더에 담았습니다.")
                return chain.proceed(builder.build())
            }

        } catch (e: Exception) {
            return chain.proceed(chain.request())
        }
        return chain.proceed(chain.request())
    }

}