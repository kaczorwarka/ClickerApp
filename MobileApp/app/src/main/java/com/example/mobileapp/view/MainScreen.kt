package com.example.mobileapp.view

import android.annotation.SuppressLint
import android.service.autofill.OnClickAction
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapp.R
import com.example.mobileapp.viewmodel.MainViewModel
import com.example.mobileapp.viewmodel.Screen

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    firstName: String?,
    lastName: String?,
    email: String?,
    amountOfLives: Int?,
    token: String?
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val viewModel = viewModel<MainViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                (return if (firstName != null && lastName != null && email != null &&amountOfLives != null && token != null) {
                    MainViewModel(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        amountOfLives = amountOfLives,
                        token = token,
                        scope = scope,
                        snackBarHostState =  snackBarHostState
                    ) as T
                } else
                    super.create(modelClass))
            }
        }
    )

    val size by animateDpAsState(
        targetValue = if (viewModel.isStartGamePressed.value) 96.dp else 128.dp,
        animationSpec = tween(durationMillis = 150), label = ""
    )

    val timerValue by viewModel.timerValue
    val lives by viewModel.lives
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "Health"
                    )
                    Text(
                        text = "x$lives",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {navController.navigate(Screen.LoginScreen.route)}) {
                        Icon(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "logout"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = !viewModel.gameStarted.value,
                enter = viewModel.enterAnimation(),
                exit = viewModel.exitAnimation()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = "Score: ${viewModel.score.intValue}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.alpha(viewModel.firstRun.floatValue)
                    )
                    Button(
                        onClick = {
                            viewModel.checkStart()
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "New Game",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = viewModel.gameStarted.value,
                enter = viewModel.enterAnimation(),
                exit = viewModel.exitAnimation()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Score: ${viewModel.score.intValue}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Timer: $timerValue",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(
                        onClick = {
                            viewModel.isStartEnabled.value = false
                            viewModel.startTimer()
                        },
                        enabled =  viewModel.isStartEnabled.value,
                        modifier = Modifier.alpha(if (viewModel.isStartEnabled.value) 1f else 0.5f)
                    ) {
                        Text(text = "Start")
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        if (!viewModel.isStartEnabled.value){
                                            viewModel.isStartGamePressed.value = true
                                            try {
                                                awaitRelease()
                                            } finally {
                                                viewModel.isStartGamePressed.value = false
                                                viewModel.incrementSore()
                                            }
                                        }
                                    }
                                )
                            }
                            .alpha(if (!viewModel.isStartEnabled.value) 1f else 0.5f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.button),
                            contentDescription = "Game Image",
                            modifier = Modifier.size(size)
                        )
                    }
                }
            }
        }
    }
}