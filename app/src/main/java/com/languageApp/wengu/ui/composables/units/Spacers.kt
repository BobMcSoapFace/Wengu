package com.languageApp.wengu.ui.composables.units

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.ui.localWindowInfo

@Composable
fun Divider(mult : Int = 1){
    Box(modifier = Modifier
        .height(localWindowInfo.current.dividerHeight * mult)
        .fillMaxWidth())
}
@Composable
fun ScreenPaddingDivider(mult : Int = 1){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(localWindowInfo.current.screenPadding * mult)
    )
}
@Composable
fun Separator(
    color : Color = MaterialTheme.colorScheme.onPrimary,
    size : Dp = 1.dp,
    modifier : Modifier = Modifier
){
    Box(
        modifier = modifier
            .height(size)
            .background(color)
    )
}