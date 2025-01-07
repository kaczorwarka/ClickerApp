package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.apiconnection.RetrofitInstance
import com.example.mobileapp.apiconnection.user.UpdateLivesRequest
import com.example.mobileapp.model.Game
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class MainViewModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    var amountOfLives: Int,
    val token: String,
    private val scope: CoroutineScope,
    private val snackBarHostState: SnackbarHostState
): ViewModel() {
    private var _lives = mutableIntStateOf(amountOfLives)
    val lives: State<Int> = _lives
    var score = mutableIntStateOf(0)
    var gameStarted = mutableStateOf(false)
    var isStartEnabled = mutableStateOf(true)
    private var animationDuration = 1000
    var isStartGamePressed = mutableStateOf(false)
    var firstRun = mutableFloatStateOf(0f)
    private var _timerValue = mutableIntStateOf(0)
    val timerValue: State<Int> = _timerValue

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

    fun checkStart() {
        if (amountOfLives <= 0){
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = "You don't have enough amount of lives",
                    duration = SnackbarDuration.Short
                )
            }
        } else {
            gameStarted.value = true
            score.intValue = 0
        }
    }

    fun startTimer() {
        firstRun.floatValue = 1f
        viewModelScope.launch {
            for (time in 10 downTo 0) {
                _timerValue.intValue = time
                delay(1000L)
            }

            isStartEnabled.value = true
            gameStarted.value = false
            isStartGamePressed.value = false

            decrementLives()
        }
    }

    private fun decrementLives() {
        scope.launch {
            val gson = Gson()
            val updateLivesRequest = UpdateLivesRequest(-1)
            val jsonBody = gson.toJson(updateLivesRequest)
            val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
            try {
                val response = RetrofitInstance.userCrud.updateLives(
                    email,
                    "Bearer $token",
                    requestBody
                )
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        _lives.intValue = user.amountOfLives
                        saveGame()
                    }

                } else {
                    snackBarHostState.showSnackbar(
                        message = "User Error: ${response.code()}",
                        duration = SnackbarDuration.Short
                    )
                }
            } catch (e: IOException) {
                Log.e(TAG, "IOException: $e")

                snackBarHostState.showSnackbar(
                    message = "Internet Connection error",
                    duration = SnackbarDuration.Short
                )
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: $e")
                snackBarHostState.showSnackbar(
                    message = "Data Base api connection error",
                    duration = SnackbarDuration.Short
                )
            }
        }

//        lives.value -= 1
    }

    private fun saveGame () {
        scope.launch {
            try {
                val date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())

                val response = RetrofitInstance.gameCrud.saveGame(
                    email,
                    "Bearer $token",
                    Game(score.intValue, SimpleDateFormat("yyyy-MM-dd").format(date) )
                )
                if (response.code() == 201) {
                    snackBarHostState.showSnackbar(
                        message = "Game Saved !",
                        duration = SnackbarDuration.Short
                    )
                } else {
                    snackBarHostState.showSnackbar(
                        message = "Game Error: ${response.code()}",
                        duration = SnackbarDuration.Short
                    )
                }
            } catch (e: IOException) {
                Log.e(TAG, "IOException: $e")

                snackBarHostState.showSnackbar(
                    message = "Internet Connection error",
                    duration = SnackbarDuration.Short
                )
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: $e")
                snackBarHostState.showSnackbar(
                    message = "Data Base api connection error",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}