package com.example.mobileapp.viewmodel

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val amountOfLives: Int,
    val token: String
): ViewModel() {
    var lives = mutableIntStateOf(amountOfLives)
    var score = mutableIntStateOf(0)
    var gameStarted = mutableStateOf(false)
    var isStartEnabled = mutableStateOf(true)
    var animationDuration = 1000
    var isStartGamePressed = mutableStateOf(false)
    var firstRun = mutableFloatStateOf(0f)
    private val _timerValue = MutableStateFlow(0) //timer aktualizuje się dopierojak kliknę przycisk
    val timerValue: StateFlow<Int> = _timerValue

    fun decrementLives() {
        lives.value -= 1
    }

    fun incrementSore() {
        if (!isStartEnabled.value){
            score.value += 1
        }
    }

    fun exitAnimation(): ExitTransition {
        return slideOutVertically(
            animationSpec = tween(durationMillis = animationDuration)
        ) + fadeOut(animationSpec = tween(durationMillis = animationDuration))
    }

    fun enterAnimation(): EnterTransition {
        return slideInVertically(
            animationSpec = tween(durationMillis = animationDuration)
        ) + fadeIn(animationSpec = tween(durationMillis = animationDuration))
    }

    fun startTimer() {
        firstRun.floatValue = 1f
        viewModelScope.launch {
            for (time in 10 downTo 0) {
                _timerValue.value = time
                delay(1000L)
            }

            isStartEnabled.value = true
            gameStarted.value = false
            isStartGamePressed.value = false
        }
    }

}