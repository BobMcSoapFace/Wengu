package com.languageApp.wengu.ui.composables.units.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.modules.DialogPromptType
import com.languageApp.wengu.ui.AnimateState
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch

@Composable
inline fun <reified T>SelectionButton(
    item : State<T>,
    optionList : State<List<T>>,
    label : String = "Label",
    message : String = "Choose a $label",
    crossinline toString :  (T) -> String = {it.toString()},
    crossinline onSelect :  (T) -> Unit,
    modifier : Modifier = Modifier,
    buttonColor : Color = MaterialTheme.colorScheme.primary,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    listColor : Color = textColor,
    listTextColor : Color = buttonColor,
){
    val dialogCoroutine = rememberCoroutineScope()
    Column(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .padding(localWindowInfo.current.buttonAnimateSize)
            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
            .background(buttonColor)
            .padding(localWindowInfo.current.closeOffset)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.textFieldHeight)
                .padding(localWindowInfo.current.closeOffset)
            , contentAlignment = Alignment.BottomCenter
        ){
            Text(
                text = label,
                color = textColor,
                style = localWindowInfo.current.fieldLabeltextStyle,
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.85f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.textFieldHeight)
                .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                .background(listColor)
                .clickable {
                    dialogCoroutine.launch {
                        DialogPrompt.sendDialog(
                            DialogPrompt(
                                message = message,
                                type = DialogPromptType.LIST_SELECTION(
                                    list = optionList.value.map { it.toString() },
                                    selectItem = {str ->
                                        onSelect(optionList.value.find { str == it.toString() } ?: optionList.value[0])
                                    }
                                ),

                            )
                        )
                    }
                }
                .padding(localWindowInfo.current.slightOffset)
        ){
            Text(
                text = toString(item.value),
                color = listTextColor,
                style = localWindowInfo.current.fieldLabeltextStyle,
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.Center)
            )
        }
    }
}