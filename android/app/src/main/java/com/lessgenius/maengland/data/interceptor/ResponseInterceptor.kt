package com.lessgenius.maengland.data.interceptor

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lessgenius.maengland.data.datasource.local.PreferencesManager
import com.lessgenius.maengland.data.datasource.local.PreferencesManager.Companion.ACCESS_TOKEN
import com.lessgenius.maengland.data.datasource.local.PreferencesManager.Companion.REFRESH_TOKEN
import com.lessgenius.maengland.data.datasource.remote.AccountService
import com.lessgenius.maengland.data.model.ErrorResponse
import com.lessgenius.maengland.data.model.RefreshTokenRequest
import com.lessgenius.maengland.di.NetworkModule.BASE_URL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http2.ErrorCode
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject


/**
 * 서버에 요청할 때 accessToken유효한지 검사
 * 유효하지 않다면 재발급 api 호출
 * refreshToken이 유효하다면 정상적으로 accessToken재발급 후 기존 api 동작 완료
 */
private const val TAG = "ResponseInterceptor_김진영"

class ResponseInterceptor @Inject constructor(
    private val preferences: PreferencesManager
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        var accessToken = ""
        var isRefreshable = false

        Log.d(TAG, "intercept: 지금 코드 ${response.code}")
        Log.d(TAG, "intercept: 지금 네트워크 리스폰스 ${response.networkResponse}")

        when (response.code) {
            400 -> {
                val errorResponse = parseErrorResponse(response.body)
//                CoroutineScope(Dispatchers.IO).launch {
//                    when (errorResponse.errorCode) {
//                        "S-003" -> { // 유효하지 않은 가격 범위 에러
//                            throw IOException("S-003")
//                        }
//                    }
//                }
            }

            401 -> { // 여러 에러들 종합 (에러 메시지로 확인하자.)
                val errorResponse = parseErrorResponse(response.body)
                Log.d(TAG, "intercept: 에러 바디 파싱 ${errorResponse}")

                Log.d(TAG, "intercept: 에러(401) : 만료된 토큰입니다.")
                runBlocking {
                    //토큰 갱신 api 호출
                    Log.d(TAG, "intercept: ${preferences.getToken(REFRESH_TOKEN)}")
                    preferences.getToken(REFRESH_TOKEN)?.let {
                        Log.d(TAG, "intercept: ${preferences.getToken(REFRESH_TOKEN)}")

                        val result = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(
                                GsonConverterFactory.create(
                                    GsonBuilder()
                                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                        .setLenient()
                                        .create()
                                )
                            )
                            .build()
                            .create(AccountService::class.java).postRefreshToken(
                                RefreshTokenRequest(preferences.getToken(REFRESH_TOKEN).first())
                            )

                        Log.d(
                            TAG, "intercept 현재 찐 refresh: ${
                                preferences.getToken(
                                    REFRESH_TOKEN
                                )
                            }"
                        )
                        if (result.watchAccessToken.isNotEmpty()) {
                            Log.d(TAG, "intercept: 다시 받아오는데 성공했어요!!!!!!")
                            preferences.updateToken(ACCESS_TOKEN, result.watchAccessToken)
                            preferences.updateToken(REFRESH_TOKEN, result.watchRefreshToken)
                            Log.d(TAG, "intercept: 만료된 토큰 다시 받은거 $result")
                            accessToken = result.watchAccessToken
                            isRefreshable = true
                        }
                        if (result.equals(
                                ErrorResponse(
                                    httpStatus = 401,
                                    errorCode = "A-005",
                                    errorMessage = "해당 refresh token은 존재하지 않습니다."
                                )
                            )
                        ) {
                            Log.d(TAG, "intercept: 리프레시 토큰으로 다시 받아오는 코드 실패입니다.")
//                                    Log.d(TAG, "intercept success : ${result.isSuccessful}")
//                            throw (IOException("refresh_exception"))
                        }
                    }
                }
            }

            500 -> {

            }
        }

        // 다시 내가 호출했었던 거 호출하는 로직 필요할듯?
        if (isRefreshable) {
            Log.d(TAG, "intercept: 리프레시가 알맞게 통신했고, 새 엑세스토큰으로 가능하다는 소리입니다~")
            val newRequest =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken")
                    .build()
            return chain.proceed(newRequest)
        }

        return response
    }

    private fun parseErrorResponse(responseBody: ResponseBody?): ErrorResponse {
        val gson = Gson()
        return gson.fromJson(responseBody?.charStream(), ErrorResponse::class.java)
    }
}