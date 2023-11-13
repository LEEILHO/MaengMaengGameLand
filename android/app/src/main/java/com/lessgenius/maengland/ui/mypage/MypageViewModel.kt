package com.lessgenius.maengland.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lessgenius.maengland.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    ViewModel() {
    private var _loginStatusResponse = MutableStateFlow(true)
    val loginStatusResponse = _loginStatusResponse.asStateFlow()

    fun logout() = viewModelScope.launch {
        accountRepository.logout()
        if (accountRepository.getLoginStatus().watchAccessToken.isEmpty()) {
            _loginStatusResponse.emit(false)
        } else {
            _loginStatusResponse.emit(true)
        }

    }
}