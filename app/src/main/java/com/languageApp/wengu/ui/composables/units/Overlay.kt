package com.languageApp.wengu.ui.composables.units

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.ui.AnimateState
import kotlinx.coroutines.launch

@Composable
fun Overlay(
    modifier : Modifier = Modifier,
){
    val coroutineScope = rememberCoroutineScope()
    val animState = AnimateState.localAnimateState.current
    val transparency = animateIntAsState(
        targetValue = (255*animState.overlayVisibility).toInt(),
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearOutSlowInEasing
        ),
        label = "Overlay"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0, 0, 0, transparency.value))
            .then(
                if (transparency.value > 1) Modifier.clickable(null, null) {
                    coroutineScope.launch {
                        AnimateState.setAnimateState(animState.copy(overlayVisibility = 0.0f))
                        DialogPrompt.sendDialog(null)
                    }
                }
                else Modifier
            )
    )
}