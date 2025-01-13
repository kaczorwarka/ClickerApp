package com.example.mobileapp.viewmodel

sealed class Screen(val route: String) {
    data object LoginScreen: Screen("login_screen")
    data object MainScreen: Screen("main_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}