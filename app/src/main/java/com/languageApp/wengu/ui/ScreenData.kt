package com.languageApp.wengu.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
@Stable
data class ScreenNavigationObject(
    val route : String,
    val displayName : String = route,
    val displayIcon: ImageVector = Icons.Default.Warning
)


