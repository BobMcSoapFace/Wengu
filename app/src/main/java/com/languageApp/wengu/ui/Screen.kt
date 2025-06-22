package com.languageApp.wengu.ui

import androidx.compose.runtime.Composable

class Screen(
    val route: String,
    val screen : @Composable () -> Unit
) {

    fun withArgs(vararg args: String) : String {
        return buildString {
            append(route)
            args.forEach {arg ->
                append("/$arg")
            }
        }
    }
}