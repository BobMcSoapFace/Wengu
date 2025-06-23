package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.modules.DialogPromptType
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.NavigationBar
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch

enum class HomepageState {
    HOMEPAGE,
    VOCAB,
    SETTINGS,
}
@Composable
fun HomepageScreen(
    navigateTo : (String) -> Unit,
){
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    var homepageState by remember {
        mutableStateOf(HomepageState.HOMEPAGE)
    }
    val testScope = rememberCoroutineScope()
    val navigateables : List<InteractableIcon> = remember {
        listOf(
            InteractableIcon({}, "Home", primary, onPrimary, icon=Icons.Default.Home),
            InteractableIcon({}, "Vocab", primary, onPrimary, icon=Icons.Default.Star),
            InteractableIcon({
                testScope.launch {
                    DialogPrompt.sendDialog(
                        DialogPrompt(
                            type = DialogPromptType.NOTICE,
                            function = {},
                            message = "Test dialog message",
                        )
                    )
                }
            }, "Test", primary, onPrimary, icon=Icons.Default.Email),
            InteractableIcon({}, "Settings", primary, onPrimary, icon=Icons.Default.MoreVert)
        )
    }
    Box(modifier = Modifier
        .fillMaxSize()
    ){
        LazyColumn(
            modifier=Modifier
                .fillMaxSize()
        ) {

        }
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(localWindowInfo.current.navigateBarHeight),
            buttons = navigateables
        )
    }
}
