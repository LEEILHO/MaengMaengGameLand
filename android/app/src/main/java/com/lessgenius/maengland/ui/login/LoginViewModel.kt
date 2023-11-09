package com.lessgenius.maengland.ui.login

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

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    ViewModel() {
        private var _loginResponse = MutableStateFlow<NetworkResult<Token>>(NetworkResult.Idle)
        val loginResponse = _loginResponse.asStateFlow()


    fun login(code: String) = viewModelScope.launch {
        _loginResponse.emit(accountRepository.login(RequestCode(code)))
    }
}