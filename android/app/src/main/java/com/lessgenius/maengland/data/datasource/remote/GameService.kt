package com.lessgenius.maengland.data.datasource.remote

import com.lessgenius.maengland.data.model.RequestScore
import retrofit2.http.Body
import retrofit2.http.POST

interface GameService {
    @POST("record/watch")
    suspend fun recordScore(@Body requestScore: RequestScore)
}