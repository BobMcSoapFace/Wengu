package com.languageApp.wengu.ui.composables.units.buttons

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowColumnOverflow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.modules.DialogPromptType
import com.languageApp.wengu.ui.AnimateState
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ListSelection(
    list : State<List<T>>,
    selected : State<List<T>>,
    onClick : (String) -> Unit,
    onSelect : (String) -> Unit,
    label : String = "Label",
    height : Dp,
    width : Dp,
    modifier : Modifier = Modifier,
    buttonColor : Color = MaterialTheme.colorScheme.primary,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    onButtonColor : Color = textColor,
    onTextColor : Color = buttonColor,
    toString : (T) -> String = {it.toString()},
    limit : Int = -1
){

    val isSelected = remember {mutableStateOf(false)}
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
    Box(
        modifier = modifier
            .width(width)
            //.height(IntrinsicSize.Max)
            .padding(localWindowInfo.current.buttonAnimateSize)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding.dp))
                .background(backgroundColorAnimValue.value)
                .padding(localWindowInfo.current.closeOffset)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
            ){
                Text(
                    text = label,
                    color = textColorAnimValue.value,
                    style = localWindowInfo.current.fieldLabeltextStyle,
                    modifier = Modifier
                        .align(
                            if(localWindowInfo.current.screenHeightInfo == WindowInfo.WindowType.Compact) Alignment.Center else Alignment.CenterStart
                        )
                )
            }
        }
        FlowColumn(
            modifier = modifier
                .fillMaxWidth(0.6f)
                .align(Alignment.CenterEnd)
                .clip(
                    RoundedCornerShape(
                        topEnd = localWindowInfo.current.buttonRounding.dp,
                        bottomEnd = localWindowInfo.current.buttonRounding.dp,
                        bottomStart = if (selected.value.isNotEmpty()) localWindowInfo.current.buttonRounding.dp else 0.dp,
                    )
                )
                .background(Color.Transparent)
                .border(1.dp, backgroundColorAnimValue.value)
            , overflow = FlowColumnOverflow.Visible
        ) {
            selected.value.forEachIndexed {_, it ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .background(textColorAnimValue.value)
                        .clickable {
                            onClick(it.toString())
                        }
                ) {
                    Text(
                        text = toString(it),
                        color = backgroundColorAnimValue.value,
                        style = localWindowInfo.current.fieldLabeltextStyle,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            if((list.value - selected.value.toSet()).isNotEmpty() && (limit < 0 || (selected.value.size<limit))){
                val addScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .background(textColorAnimValue.value)
                        .clickable {
                            addScope.launch {
                                DialogPrompt.sendDialog(
                                    DialogPrompt(
                                        message = "Select vocabulary types to apply",
                                        type = DialogPromptType.LIST_SELECTION(
                                            list = (list.value - selected.value.toSet()).map { it.toString() },
                                            selectItem = onSelect
                                        )
                                    )
                                )
                            }
                        }
                ) {
                    Text(
                        text = "+ Add",
                        color = backgroundColorAnimValue.value,
                        style = localWindowInfo.current.fieldLabeltextStyle,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}