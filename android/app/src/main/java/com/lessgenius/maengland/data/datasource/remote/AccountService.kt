package com.lessgenius.maengland.data.datasource.remote


import com.lessgenius.maengland.data.model.RefreshTokenRequest
import com.lessgenius.maengland.data.model.RequestCode
import com.lessgenius.maengland.data.model.Token
import com.lessgenius.maengland.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountService {

    @POST("auth/watch")
    suspend fun login(@Body watchCode: RequestCode): Token

    @POST("auth/watch-token")
    suspend fun postRefreshToken(@Body watchRefreshToken: RefreshTokenRequest): Token

    @GET("user/info")
    suspend fun getUserInfo(): User
}