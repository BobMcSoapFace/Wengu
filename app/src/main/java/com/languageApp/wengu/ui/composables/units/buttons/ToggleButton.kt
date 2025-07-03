package com.languageApp.wengu.ui.composables.units.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.ui.localWindowInfo

@Composable
fun ToggleButton(
    label : String = "Label",
    boolValue : State<Boolean?>,
    onClick : () -> Unit,
    buttonColor : Color = MaterialTheme.colorScheme.onPrimary,
    textColor : Color = MaterialTheme.colorScheme.primary,
    onButtonColor : Color = textColor,
    onTextColor : Color = buttonColor,
    modifier : Modifier = Modifier,
    border : Boolean = false,
){
    val backgroundColorAnimValue = animateColorAsState(targetValue =
    if(boolValue.value==true) onButtonColor
    else buttonColor,
        tween(
            durationMillis = 200,
            easing = LinearOutSlowInEasing
        ), label = "toggleNumberSelection")
    val textColorAnimValue = animateColorAsState(targetValue =
    if(boolValue.value==true) onTextColor
    else textColor,
        tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        ), label = "toggleNumberSelection")
    Box(
        modifier = modifier
            .padding(localWindowInfo.current.buttonAnimateSize)
            .then(if(!border) Modifier else Modifier.border(1.dp, textColorAnimValue.value, RoundedCornerShape(
                localWindowInfo.current.buttonRounding)))
            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
            .background(backgroundColorAnimValue.value)
            .clickable { onClick() }
            .padding(localWindowInfo.current.closeOffset)
        , contentAlignment = Alignment.Center
    ){
        Text(
            text = label,
            style = localWindowInfo.current.fieldLabeltextStyle,
            color = textColorAnimValue.value,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(0.65f)
        )
        if(boolValue.value==true)
        Icon(
            imageVector = Icons.Default.Check,
            tint = textColorAnimValue.value,
            contentDescription = "$label toggle button",
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .aspectRatio(1f, true)
                .align(Alignment.CenterEnd)
        )
    }
}