package com.lessgenius.maengland.data.repository

import android.util.Log
import com.lessgenius.maengland.data.datasource.local.PreferencesManager
import com.lessgenius.maengland.data.datasource.local.PreferencesManager.Companion.ACCESS_TOKEN
import com.lessgenius.maengland.data.datasource.local.PreferencesManager.Companion.REFRESH_TOKEN
import com.lessgenius.maengland.data.datasource.remote.AccountService
import com.lessgenius.maengland.data.datasource.remote.handleApi
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestCode
import com.lessgenius.maengland.data.model.Token
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val TAG = "AccountRepositoryImpl_김진영"

class AccountRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val dataStore: PreferencesManager
) :
    AccountRepository {

    override suspend fun login(watchCode: RequestCode): NetworkResult<Token> {
        return handleApi {
            accountService.login(watchCode)
        }
    }

    override suspend fun updateJwtTokens(jwtToken: Token) {
        dataStore.updateToken(ACCESS_TOKEN, jwtToken.watchAccessToken)
        dataStore.updateToken(REFRESH_TOKEN, jwtToken.watchRefreshToken)
        Log.d(TAG, "updateJwtTokens: ")
    }

    override suspend fun getLoginStatus(): Token {
        val jwtAccessToken = dataStore.getToken(ACCESS_TOKEN).first()
        val jwtRefreshToken = dataStore.getToken(REFRESH_TOKEN).first()

        Log.d(TAG, "getLoginStatus: ${jwtAccessToken.toString()} ${jwtRefreshToken.toString()}")

        return Token(jwtAccessToken.toString(), jwtRefreshToken.toString())
    }

    override suspend fun logout() {
        dataStore.remove(ACCESS_TOKEN)
        dataStore.remove(REFRESH_TOKEN)
    }
}