package com.languageApp.wengu.ui.composables.units.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.localWindowInfo

@Composable
fun IconButton(
    iconInteractableIcon: InteractableIcon,
    modifier: Modifier,
){
    var clicked by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if(clicked) iconInteractableIcon.onColor else iconInteractableIcon.color,
        animationSpec = tween(durationMillis = if(clicked) 2 else 200, easing = LinearEasing), label = "${iconInteractableIcon.label} icon button color"
    )
    val textColor by animateColorAsState(
        targetValue = if(clicked) iconInteractableIcon.onTextColor else iconInteractableIcon.textColor,
        animationSpec = tween(durationMillis = if(clicked) 2 else 200, easing = LinearEasing), label = "${iconInteractableIcon.label} icon button text color",
        finishedListener = {clicked = false}
    )
    val size by animateDpAsState(
        targetValue = if(clicked) 0.dp else localWindowInfo.current.buttonAnimateSize,
        animationSpec = tween(durationMillis = if(clicked) 10 else 120, easing = LinearOutSlowInEasing), label = "${iconInteractableIcon.label} icon button text color",
        finishedListener = {clicked = false}
    )
    Box(
        modifier = modifier
            .padding(size)
            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
            .background(color)
            .clickable {
                clicked = true
                iconInteractableIcon.function()
            }
            .padding(localWindowInfo.current.buttonPadding),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = iconInteractableIcon.icon,
            contentDescription = iconInteractableIcon.label,
            tint = textColor,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .aspectRatio(1f, true)
                .align(Alignment.TopCenter)
        )
        Text(
            text = iconInteractableIcon.label,
            textAlign = TextAlign.Center,
            color = textColor,
            style = localWindowInfo.current.buttonTextStyle,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
    }
}