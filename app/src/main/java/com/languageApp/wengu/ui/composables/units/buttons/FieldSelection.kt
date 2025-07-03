package com.languageApp.wengu.ui.composables.units.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.ui.localWindowInfo

/**
 * Text inputtable field
 * @param isSelected Parameter determines whether button is highlighted
 */
@Composable
fun FieldSelection(
    inputValue : String,
    onInputChange : (String) -> Unit,
    label : String = "Value:",
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    onButtonColor : Color = textColor,
    onTextColor : Color = buttonColor,
    modifier : Modifier = Modifier,
    noLabel : Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isSelected = interactionSource.collectIsFocusedAsState()
    val backgroundColorAnimValue = animateColorAsState(targetValue =
    if(isSelected.value) onButtonColor
    else buttonColor,
        tween(
            durationMillis = 200,
            easing = LinearOutSlowInEasing
        ), label = "toggleNumberSelection")
    val textColorAnimValue = animateColorAsState(targetValue =
    if(isSelected.value) onTextColor
    else textColor,
        tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        ), label = "toggleNumberSelection")
    var textHeightDp by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .padding(localWindowInfo.current.buttonAnimateSize)
            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding.dp))
            .background(backgroundColorAnimValue.value)
            .padding(
                horizontal = localWindowInfo.current.closeOffset,
                vertical = localWindowInfo.current.closeOffset / 2
            )
        , horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        if(!noLabel) {
            Text(
                text = label,
                color = textColorAnimValue.value,
                style = localWindowInfo.current.fieldLabeltextStyle,
                modifier = Modifier.onGloballyPositioned {
                    // Get the height of the Text in Dp
                    textHeightDp = with(localDensity) { it.size.height.toDp() }
                }
            )
            Spacer(Modifier.fillMaxWidth(0.075f))
        }
        BasicTextField(
            value = inputValue, onValueChange = {
                onInputChange(it)
            },
            interactionSource = interactionSource,
            cursorBrush = SolidColor(textColorAnimValue.value),
            singleLine = true,
            textStyle = localWindowInfo.current.fieldLabeltextStyle + TextStyle(color = textColorAnimValue.value),
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(localWindowInfo.current.fieldPadding)
            ,
        )
    }
}