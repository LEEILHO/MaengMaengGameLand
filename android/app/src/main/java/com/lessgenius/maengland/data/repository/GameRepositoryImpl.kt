package com.lessgenius.maengland.data.repository

import com.lessgenius.maengland.data.datasource.remote.GameService
import com.lessgenius.maengland.data.datasource.remote.handleApi
import com.lessgenius.maengland.data.model.BestScore
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestScore
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(private val gameService: GameService) :
    GameRepository {
    override suspend fun recordScore(requestScore: RequestScore): NetworkResult<Unit> {
        return handleApi {
            gameService.recordScore(requestScore)
        }
    }

}