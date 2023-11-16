package com.lessgenius.maengland.data.repository

import androidx.datastore.preferences.core.Preferences
import com.lessgenius.maengland.data.model.BestScore
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestCode
import com.lessgenius.maengland.data.model.Token
import com.lessgenius.maengland.data.model.User
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface AccountRepository {
    suspend fun login(watchCode: RequestCode): NetworkResult<Token>

    // 토큰 값 저장
    suspend fun updateJwtTokens(jwtToken: Token)

    // 로그인 유무 확인
    suspend fun getLoginStatus(): Token

    // 로그아웃
    suspend fun logout()

    // 유저 정보 확인
    suspend fun getUserInfo(): NetworkResult<User>

    // 최고 점수 확인
    suspend fun getBestScore() : NetworkResult<BestScore>
}