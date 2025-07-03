package com.languageApp.wengu.ui.composables.units.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.ui.localWindowInfo

@Composable
fun CounterButton(
    counter : MutableIntState,
    lowerLimit : Int = 0,
    upperLimit : Int = Int.MAX_VALUE,
    label : String = "Label",
    modifier : Modifier = Modifier,
    backgroundColor : Color = MaterialTheme.colorScheme.primary,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    buttonColor : Color = textColor,
    buttonTextColor : Color = backgroundColor,
    onButtonColor : Color = buttonTextColor,
    onTextColor : Color = buttonColor,
    border : Color? = null,
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding.dp))
            .then(
                if(border!=null) Modifier.border(1.dp, border, RoundedCornerShape(localWindowInfo.current.buttonRounding.dp))
                else Modifier
            )
            .background(backgroundColor)
    ){
        Text(
            text = "$label\n${counter.intValue}",
            style = localWindowInfo.current.fieldLabeltextStyle,
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier.align(Alignment.Center),
        )
        var clickedDown by remember { mutableStateOf(false) }
        val colorDown by animateColorAsState(
            targetValue = if(clickedDown) onButtonColor else buttonColor,
            animationSpec = tween(durationMillis = if(clickedDown) 2 else 500, easing = LinearEasing), label = "$label down counter button color"
        )
        val textColorDown by animateColorAsState(
            targetValue = if(clickedDown) onTextColor else buttonTextColor,
            animationSpec = tween(durationMillis = if(clickedDown) 2 else 500, easing = LinearEasing), label = "$label down counter button text color",
            finishedListener = {clickedDown = false}
        )
        var clickedUp by remember { mutableStateOf(false) }
        val colorUp by animateColorAsState(
            targetValue = if(clickedUp) onButtonColor else buttonColor,
            animationSpec = tween(durationMillis = if(clickedUp) 2 else 500, easing = LinearEasing), label = "$label up counter button color"
        )
        val textColorUp by animateColorAsState(
            targetValue = if(clickedUp) onTextColor else buttonTextColor,
            animationSpec = tween(durationMillis = if(clickedUp) 2 else 500, easing = LinearEasing), label = "$label up counter button text color",
            finishedListener = {clickedUp = false}
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(localWindowInfo.current.counterButtonSize)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(
                    bottomStart = localWindowInfo.current.buttonRounding.dp,
                    topStart = localWindowInfo.current.buttonRounding.dp)
                )
                .background(colorDown)
                .clickable(null, null) {
                    clickedDown=true
                    if(counter.intValue>lowerLimit){
                        counter.intValue--
                    }
                }
                .padding(localWindowInfo.current.slightOffset)
        ){
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "$label counter decrease",
                tint = textColorDown,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(localWindowInfo.current.counterButtonSize)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(
                    topEnd = localWindowInfo.current.buttonRounding.dp,
                    bottomEnd = localWindowInfo.current.buttonRounding.dp)
                )
                .background(colorUp)
                .clickable(null, null) {
                    clickedUp=true
                    if(counter.intValue<upperLimit){
                        counter.intValue++
                    }
                }
                .padding(localWindowInfo.current.slightOffset)
        ){
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "$label counter increase",
                tint = textColorUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .align(Alignment.Center)
            )
        }
    }
}