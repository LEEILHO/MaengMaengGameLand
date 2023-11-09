package com.lessgenius.maengland.data.repository

import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestCode
import com.lessgenius.maengland.data.model.Token
import retrofit2.Response
import retrofit2.http.Body

interface AccountRepository {
    suspend fun login(@Body watchCode: RequestCode) : NetworkResult<Token>
}