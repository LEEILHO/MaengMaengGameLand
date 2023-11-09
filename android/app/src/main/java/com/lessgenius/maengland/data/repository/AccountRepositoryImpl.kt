package com.lessgenius.maengland.data.repository

import com.lessgenius.maengland.data.datasource.remote.AccountService
import com.lessgenius.maengland.data.datasource.remote.handleApi
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestCode
import com.lessgenius.maengland.data.model.Token
import retrofit2.Response
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountService: AccountService) :
    AccountRepository {
    override suspend fun login(watchCode: RequestCode): NetworkResult<Token> {
        return handleApi {
            accountService.login(watchCode)
        }
    }
}