package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mobileapp.apiconnection.RetrofitInstance
import com.example.mobileapp.apiconnection.auth.AuthenticationRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

const val TAG = "LoginActivity"

class LoginViewModel: ViewModel() {

    var emailTextField = mutableStateOf("")
    var passwordTextField = mutableStateOf("")
    var token: String ?= ""

    fun onSignInClick(snackBarHostState: SnackbarHostState, navController: NavController, scope: CoroutineScope) {
        val temporaryEmail = emailTextField.value
        val temporaryPassword = passwordTextField.value
        scope.launch {
                try{
                    val response = RetrofitInstance.authCrud.getToken(AuthenticationRequest(temporaryEmail, temporaryPassword))
                    if (response.isSuccessful){
                        val authResponse = response.body()
                        token = authResponse?.token
                        token?.let { getUser(temporaryEmail, it, snackBarHostState, navController, scope) }

//                        snackBarHostState.showSnackbar(
//                            message = "Token ${authResponse?.token}",
//                            duration = SnackbarDuration.Short
//                        )
                    } else {
                        snackBarHostState.showSnackbar(
                            message = "Error: ${response.code()}",
                            duration = SnackbarDuration.Short
                        )
                    }
                }catch (e: IOException) {
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

        emailTextField.value = ""
        passwordTextField.value = ""
    }

    private fun getUser(email: String,
                        token: String,
                        snackBarHostState: SnackbarHostState,
                        navController: NavController,
                        scope: CoroutineScope) {
        scope.launch {
            try {
                val response = RetrofitInstance.getUser.getUser(email, "Bearer $token")
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                         navController.navigate(Screen.MainScreen.withArgs(
                            user.firstName,
                            user.lastName,
                            user.email,
                            user.amountOfLives.toString(),
                            token
                        ))
                    }
//                    snackBarHostState.showSnackbar(
//                        message = "Hello: ${user?.firstName}",
//                        duration = SnackbarDuration.Short
//                    )
                } else {
                    snackBarHostState.showSnackbar(
                        message = "Error: ${response.code()}",
                        duration = SnackbarDuration.Short
                    )
                }

            }catch (e: IOException) {
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