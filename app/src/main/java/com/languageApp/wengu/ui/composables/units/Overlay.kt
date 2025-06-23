package com.languageApp.wengu.ui.composables.units

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.languageApp.wengu.ui.AnimateState

@Composable
fun Overlay(
    modifier : Modifier = Modifier,
){
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
    )
}