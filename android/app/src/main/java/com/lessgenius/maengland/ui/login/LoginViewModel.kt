package com.lessgenius.maengland.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestCode
import com.lessgenius.maengland.data.model.Token
import com.lessgenius.maengland.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel_김진영"
@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    ViewModel() {
    private var _loginResponse = MutableStateFlow<NetworkResult<Token>>(NetworkResult.Idle)
    val loginResponse = _loginResponse.asStateFlow()

    private var _loginStatusResponse = MutableStateFlow<Token?>(null)
    val loginStatusResponse = _loginStatusResponse.asStateFlow()

    fun login(code: String) = viewModelScope.launch {
        _loginResponse.emit(accountRepository.login(RequestCode(code)))
        _loginResponse.emit(NetworkResult.Idle)
    }

    fun updateToken(token: Token) = viewModelScope.launch {
        accountRepository.updateJwtTokens(token)
        _loginStatusResponse.emit(accountRepository.getLoginStatus())
    }

    fun getLoginStatus() = viewModelScope.launch {
        _loginStatusResponse.emit(accountRepository.getLoginStatus())
    }
}