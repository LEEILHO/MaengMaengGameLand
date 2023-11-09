package com.lessgenius.maengland.data.interceptor

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http2.ErrorCode
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * 서버에 요청할 때 accessToken유효한지 검사
 * 유효하지 않다면 재발급 api 호출
 * refreshToken이 유효하다면 정상적으로 accessToken재발급 후 기존 api 동작 완료
 */
private const val TAG = "ResponseInterceptor_싸피"
class ResponseInterceptor: Interceptor {
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
                Log.d(TAG, "intercept: 에러 : 400 에러입니다.")
            }

//            401 -> { // 여러 에러들 종합 (에러 메시지로 확인하자.)
//                val errorResponse = parseErrorResponse(response.body)
//                Log.d(TAG, "intercept: 에러 바디 파싱 !!!!!!!!!! ${errorResponse}")
//
//                // 에러 코드로 분기
//                when (errorResponse.errorCode) {
//                    "Auth-001" -> { // 엑세스 토큰 만료 신호
//                        Log.d(TAG, "intercept: 에러(Auth-001) : 만료된 토큰입니다.")
//                        runBlocking {
//                            //토큰 갱신 api 호출
//
//                            Log.d(TAG, "intercept: ${sharedPreferences.getString(X_REFRESH_TOKEN)}")
//                            sharedPreferences.getString(X_REFRESH_TOKEN)?.let {
//                                Log.d(TAG, "intercept: ${sharedPreferences.getString(X_REFRESH_TOKEN)}")
//
//                                val result = Retrofit.Builder()
//                                    .baseUrl(BASE_URL)
//                                    .addConverterFactory(GsonConverterFactory.create())
//                                    .build()
//                                    .create(BaseService::class.java).postRefreshToken("Bearer ${it}")
//
//                                Log.d(
//                                    TAG, "intercept 현재 찐 refresh: ${
//                                        sharedPreferences.getString(
//                                            X_REFRESH_TOKEN
//                                        )
//                                    }"
//                                )
//                                if (result.isSuccessful) {
//                                    Log.d(TAG, "intercept: 다시 받아오는데 성공했어요!!!!!!")
//                                    sharedPreferences.putString("access_token", result.body()!!.accessToken)
//                                    Log.d(TAG, "intercept: 만료된 토큰 다시 받은거 ${result.body()!!.accessToken}")
//                                    accessToken = result.body()!!.accessToken
//                                    isRefreshable = true
//                                }
//                                if (result.body() == null) {
//                                    Log.d(TAG, "intercept: 리프레시 토큰으로 다시 받아오는 코드 실패입니다.")
//                                    throw (IOException("refresh_exception"))
//                                }
//                            }
//                        }
//                    }
//                    "Auth-004" -> { // 엑세스 토큰 invalid 신호
//                        Log.d(TAG, "intercept: 에러(Auth-004) : 해당 토큰은 엑세스 토큰이 아닙니다.")
//                    }
//                    "Auth-007" -> {
//                        throw (IOException("refresh_exception"))
//                    }
//                }
//            }

//            403 -> {
//                Log.d(TAG, "intercept: 에러 : 403 에러입니다.")
//                val errorResponse = parseErrorResponse(response.body)
//                Log.d(TAG, "intercept: 에러 바디 파싱 !!!!!!!!!! ${errorResponse}")
//
//                // 에러 코드로 분기
//                when (errorResponse.errorCode) {
//                    "Auth-009" -> {
//                        Log.d(TAG, "intercept: 다시 로그인 해야합니다.")
//                        throw (IOException("required_re_login"))
//                    }
//                }
//            }

            404 -> {
                Log.d(TAG, "intercept: 에러 : 404 에러입니다.")
            }

            500 -> { // 서버에러
                Log.d(TAG, "intercept: 에러 : 500 에러입니다.")
            }
        }

        // 다시 내가 호출했었던 거 호출하는 로직 필요할듯?
        if(isRefreshable) {
            Log.d(TAG, "intercept: 리프레시가 알맞게 통신했고, 새 엑세스토큰으로 가능하다는 소리입니다~")
            val newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken").build()
            return chain.proceed(newRequest)
        }

        return response
    }

//    private fun parseErrorResponse(responseBody: ResponseBody?): ErrorResponse {
//        val gson = Gson()
//        return gson.fromJson(responseBody?.charStream(), ErrorResponse::class.java)
//    }
}