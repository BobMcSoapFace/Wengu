package com.languageApp.wengu.ui

import android.content.res.Configuration
import android.widget.GridLayout.Spec
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.paging.Config
import com.languageApp.wengu.ui.theme.descriptionText
import com.languageApp.wengu.ui.theme.moduleLabel
import com.languageApp.wengu.ui.theme.moduleLabelBold

@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthInfo =  when {
            configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenHeightInfo =  when {
            configuration.screenHeightDp < 480 -> WindowInfo.WindowType.Compact
            configuration.screenHeightDp < 900 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp,
        orientation = configuration.orientation
    )
}
data class WindowInfo(
    val screenWidthInfo : WindowType,
    val screenHeightInfo : WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp,
    val orientation : Int
){
    val closeOffset : Dp = when(screenHeightInfo){
        WindowType.Compact -> 6.dp
        WindowType.Medium -> 12.dp
        WindowType.Expanded -> 20.dp
    }
    val columnItemOffset : Dp = when(screenHeightInfo){
        WindowType.Compact -> 2.dp
        WindowType.Medium -> 4.dp
        WindowType.Expanded -> 6.dp
    }
    val screenPadding : Dp = when(screenHeightInfo){
        WindowType.Compact -> 10.dp
        WindowType.Medium -> 30.dp
        WindowType.Expanded -> 55.dp
    }
    val fieldPadding : Dp = when(screenHeightInfo){
        WindowType.Compact -> 5.dp
        WindowType.Medium -> 10.dp
        WindowType.Expanded -> 15.dp
    }
    val buttonPadding : Dp = when(screenHeightInfo){
        WindowType.Compact -> 2.dp
        WindowType.Medium -> 4.dp
        WindowType.Expanded -> 8.dp
    }
    val buttonRounding : Int = when(screenHeightInfo){
        WindowType.Compact -> 15
        WindowType.Medium -> 10
        WindowType.Expanded -> 7
    }
    val buttonAnimateSize : Dp = when(screenHeightInfo){
        WindowType.Compact -> 2.dp
        WindowType.Medium -> 3.dp
        WindowType.Expanded -> 4.dp
    }
    val navigateBarHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 90.dp
        WindowType.Medium -> 85.dp
        WindowType.Expanded -> 110.dp
    }
    val vocabItemHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 60.dp
        WindowType.Medium -> 75.dp
        WindowType.Expanded -> 95.dp
    }
    val textFieldHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 60.dp
        WindowType.Medium -> 75.dp
        WindowType.Expanded -> 95.dp
    }
    val landscapeButtonWidth : Dp = when(screenHeightInfo){
        WindowType.Compact -> 80.dp
        WindowType.Medium -> 150.dp
        WindowType.Expanded -> 200.dp
    }
    val landscapeButtonHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 80.dp
        WindowType.Medium -> 75.dp
        WindowType.Expanded -> 100.dp
    }
    val dividerHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 15.dp
        WindowType.Medium -> 20.dp
        WindowType.Expanded -> 30.dp
    }
    val longButtonWidth : Dp = when(screenHeightInfo){
        WindowType.Compact -> 200.dp
        WindowType.Medium -> 325.dp
        WindowType.Expanded -> 400.dp
    }
    val longButtonHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 80.dp
        WindowType.Medium -> 75.dp
        WindowType.Expanded -> 100.dp
    }
    val dialogListHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 100.dp
        WindowType.Medium -> 155.dp
        WindowType.Expanded -> 210.dp
    }
    val dialogListItemHeight : Dp = when(screenHeightInfo){
        WindowType.Compact -> 30.dp
        WindowType.Medium -> 40.dp
        WindowType.Expanded -> 50.dp
    }

    // Text styles
    val buttonTextSize : Int = when(screenHeightInfo){
        WindowType.Compact -> 12
        WindowType.Medium -> 12
        WindowType.Expanded -> 15
    }
    val largeButtonTextSize : Int = when(screenHeightInfo){
        WindowType.Compact -> 18
        WindowType.Medium -> 24
        WindowType.Expanded -> 30
    }
    private val vocabTextSize : Int = when(screenHeightInfo){
        WindowType.Compact -> 18
        WindowType.Medium -> 24
        WindowType.Expanded -> 32
    }
    private val footnoteTextSize : Int = when(screenHeightInfo){
        WindowType.Compact -> 10
        WindowType.Medium -> 11
        WindowType.Expanded -> 15
    }
    val buttonTextStyle : TextStyle = moduleLabelBold + TextStyle(fontSize = buttonTextSize.sp)
    val vocabTextStyle : TextStyle = moduleLabelBold + TextStyle(fontSize = vocabTextSize.sp)
    val fieldLabeltextStyle = vocabTextStyle + TextStyle()
    val footnoteTextStyle : TextStyle = descriptionText + TextStyle(fontSize = footnoteTextSize.sp)


    sealed class WindowType {
        object Compact : WindowType()
        object Medium : WindowType()
        object Expanded : WindowType()
    }
    fun getMinTimes(times : Float = 1f) : Dp {
        return min(screenWidth, screenHeight).times(times)
    }
}
val localWindowInfo = compositionLocalOf {
    WindowInfo(
        WindowInfo.WindowType.Medium,
        WindowInfo.WindowType.Medium,
        1.dp,
        1.dp,
        Configuration.ORIENTATION_PORTRAIT,
    )
}