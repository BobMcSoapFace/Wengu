package com.languageApp.wengu.ui

import androidx.compose.runtime.compositionLocalOf

data class AnimateState(
    var overlayVisibility: Float = 0f,
    var navigationDrawer : Float = 0f,
){
    companion object {
        val localAnimateState = compositionLocalOf { AnimateState() }
        var setAnimateState : (AnimateState) -> Unit = {}
    }
}