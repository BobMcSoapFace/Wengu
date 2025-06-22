package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.NavigationBar
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo


@Composable
fun HomepageScreen(
    navigateTo : (String) -> Unit,
){
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val navigateables : List<InteractableIcon> = remember {
        listOf(
            InteractableIcon({}, "Button", primary, onPrimary, icon=Icons.Default.Settings),
            InteractableIcon({}, "Button", primary, onPrimary, icon=Icons.Default.Warning),
            InteractableIcon({}, "Button", primary, onPrimary, icon=Icons.Default.Email),
            InteractableIcon({}, "Button", primary, onPrimary, icon=Icons.Default.MoreVert)
        )
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
    ){
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(localWindowInfo.current.navigateBarHeight),
            buttons = navigateables
        )
    }
}