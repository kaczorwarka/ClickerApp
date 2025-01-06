package com.example.mobileapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel = viewModel<LoginViewModel>()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ){ paddingValues ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 30.dp)
        ) {
            TextField(
                value = viewModel.emailTextField.value,
                label = {
                    Text("Email")
                },
                onValueChange = {
                    viewModel.emailTextField.value = it
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.passwordTextField.value,
                label = {
                    Text("Password")
                },
                onValueChange = {
                    viewModel.passwordTextField.value = it
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.onSignInClick(snackBarHostState, navController, scope)
            }) {
                Text("Sing In")
            }
        }
    }
}