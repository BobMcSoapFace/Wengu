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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.localWindowInfo

@Composable
fun IconButton(
    iconInteractableIcon: InteractableIcon,
    modifier: Modifier,
    expanded : Boolean = false,
    showText : Boolean = true,
    buttonColor : Color = MaterialTheme.colorScheme.primary,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    onButtonColor : Color = textColor,
    onTextColor : Color = buttonColor,
    haveBackgroundBorder : Color? = null,
    iconModifier : Modifier = Modifier
){
    var clicked by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if(clicked) onButtonColor else buttonColor,
        animationSpec = tween(durationMillis = if(clicked) 2 else 200, easing = LinearEasing), label = "${iconInteractableIcon.label} icon button color"
    )
    val textColor by animateColorAsState(
        targetValue = if(clicked) onTextColor else textColor,
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
            .then(
                if(haveBackgroundBorder!=null) Modifier.border(
                    1.dp,
                    haveBackgroundBorder,
                    RoundedCornerShape(localWindowInfo.current.buttonRounding),
                )
                    else Modifier
            )
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
            modifier = iconModifier
                .fillMaxHeight(if(!expanded) 0.6f else 0.75f)
                .aspectRatio(1f, true)
                .align(if(!showText) Alignment.Center else if(!expanded) Alignment.TopCenter else Alignment.CenterStart)
        )
        if(showText) Text(
            text = iconInteractableIcon.label,
            textAlign = if(!expanded) TextAlign.Center else TextAlign.Right,
            color = textColor,
            style = localWindowInfo.current.buttonTextStyle + TextStyle(fontSize =
            if(expanded) localWindowInfo.current.largeButtonTextSize.sp else localWindowInfo.current.buttonTextSize.sp),
            softWrap = false,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .align(if(!expanded) Alignment.BottomCenter else Alignment.CenterEnd)
                .offset(x = if(!expanded) 0.dp else -localWindowInfo.current.landscapeButtonWidth/8)
                .fillMaxWidth(if(!expanded) 1f else 0.7f)
                .fillMaxHeight(if(!expanded) 0.3f else 0.5f)
        )
    }
}