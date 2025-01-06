package com.example.mobileapp.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapp.viewmodel.Screen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route){
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.MainScreen.route + "/{fistName}/{lastName}/{email}/{amountOfLives}/{token}",
        ) {entry ->
            MainScreen(
                    navController = navController,
                    firstName = entry.arguments?.getString("fistName"),
                    lastName = entry.arguments?.getString("fistName"),
                    email = entry.arguments?.getString("email"),
                    amountOfLives = entry.arguments?.getString("amountOfLives")?.toInt(),
                    token = entry.arguments?.getString("token")
            )
        }
    }
}