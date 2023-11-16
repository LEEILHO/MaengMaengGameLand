package com.lessgenius.maengland.data.repository

import com.lessgenius.maengland.data.model.BestScore
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestScore

interface GameRepository {
    suspend fun recordScore(requestScore: RequestScore): NetworkResult<Unit>

}