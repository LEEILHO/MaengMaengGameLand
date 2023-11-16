package com.lessgenius.maengland.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lessgenius.maengland.data.model.BestScore
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.Token
import com.lessgenius.maengland.data.model.User
import com.lessgenius.maengland.data.repository.AccountRepository
import com.lessgenius.maengland.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) :
    ViewModel() {
    private var _loginStatusResponse = MutableStateFlow(true)
    val loginStatusResponse = _loginStatusResponse.asStateFlow()

    private var _userInfo = MutableStateFlow<NetworkResult<User>>(NetworkResult.Idle)
    val userInfo = _userInfo.asStateFlow()

    private var _bestScoreInfo = MutableStateFlow<NetworkResult<BestScore>>(NetworkResult.Idle)
    val bestScoreInfo = _bestScoreInfo.asStateFlow()

    fun logout() = viewModelScope.launch {
        accountRepository.logout()
        if (accountRepository.getLoginStatus().watchAccessToken.isEmpty()) {
            _loginStatusResponse.emit(false)
        } else {
            _loginStatusResponse.emit(true)
        }

    }

    fun getUserInfo() = viewModelScope.launch {
        _userInfo.emit(accountRepository.getUserInfo())
    }

    fun getBestScore() = viewModelScope.launch {
        _bestScoreInfo.emit(accountRepository.getBestScore())
    }
}