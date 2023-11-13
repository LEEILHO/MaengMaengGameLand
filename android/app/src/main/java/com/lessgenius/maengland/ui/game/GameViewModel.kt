package com.lessgenius.maengland.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.RequestScore
import com.lessgenius.maengland.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "GameViewModel_김진영"
@HiltViewModel
class GameViewModel @Inject constructor(private val gameRepository: GameRepository) :
    ViewModel() {

    private var _scoreResponse = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Idle)
    val scoreResponse = _scoreResponse.asStateFlow()

    fun recordScore(requestScore: Int) = viewModelScope.launch {
        _scoreResponse.emit(gameRepository.recordScore(RequestScore(requestScore)))
        Log.d(TAG, "recordScore: ${scoreResponse.value}")
    }
}