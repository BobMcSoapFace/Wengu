package com.languageApp.wengu.modules

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import com.languageApp.wengu.modules.DialogPrompt.Companion.DIALOG_FUNCTION_DELAY
import com.languageApp.wengu.ui.AnimateState
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.buttons.FieldSelection
import com.languageApp.wengu.ui.localWindowInfo
import com.languageApp.wengu.ui.theme.descriptionText
import com.languageApp.wengu.ui.theme.moduleLabel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DialogPrompt(
    val type : DialogPromptType,
    val function : (String) -> Unit = {},
    val message : String = "",
    val confirmLabel : String = "Confirm",
) {
    companion object {
        private val _dialogSharedFlow = MutableSharedFlow<DialogPrompt?>()
        val dialogSharedFlow = _dialogSharedFlow.asSharedFlow()
        suspend fun sendDialog(dialog : DialogPrompt?) {
            _dialogSharedFlow.emit(dialog)
        }
        val DIALOG_FUNCTION_DELAY : Long = 500L
    }
}
interface DialogPromptType {
    data object CONFIRMATION : DialogPromptType
    data class LIST_SELECTION(
        val list : List<String>,
        val selectItem : (String) -> Unit // serialized version of object of T type
    ) : DialogPromptType
    data object NOTICE : DialogPromptType
    data object FIELD_SELECTION : DialogPromptType
}

// old code thats freakishly inefficient but I have to live with my choices
@Composable
fun DialogComposable(
    windowInfo: WindowInfo,
    dialogState: State<DialogPrompt?>,
    lifecycle: LifecycleCoroutineScope,
){
    val shown = rememberSaveable { mutableStateOf(false) }
    val animateState = AnimateState.localAnimateState.current
    val typedText = rememberSaveable { mutableStateOf("") }
    LaunchedEffect(key1 = true) {
        shown.value = true
    }
    Box(modifier =
    Modifier
        .fillMaxSize()
        .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ){
        AnimatedVisibility(
            shown.value,
        ) {
            Column(
                modifier = Modifier
                    .then(
                        if (windowInfo.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            Modifier
                                .fillMaxWidth(0.85f)
                        } else {
                            Modifier
                                .fillMaxWidth(0.4f)
                        }
                    )
                    .height(IntrinsicSize.Max)
                    .defaultMinSize(
                        minHeight = windowInfo.screenHeight.times(
                            if (windowInfo.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                0.05f
                            } else 0.5f
                        )
                    )
                    .clip(
                        RoundedCornerShape(
                            windowInfo.getMinTimes(
                                0.03f
                            )
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(windowInfo.getMinTimes(0.03f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dialogState.value!!.message,
                        style = descriptionText + TextStyle(
                            color =
                            MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            fontSize = windowInfo.getMinTimes(0.04f).value.sp
                        )
                    )
                }
                Box(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth(0.9f)
                        .background(MaterialTheme.colorScheme.onTertiary)
                )
                if(dialogState.value!!.type is DialogPromptType.LIST_SELECTION){
                    val listSize = remember { (dialogState.value!!.type as DialogPromptType.LIST_SELECTION).list.size }
                    LazyColumn(
                        modifier = Modifier
                            .height(localWindowInfo.current.dialogListHeight)
                            .fillMaxWidth()
                            .padding(localWindowInfo.current.closeOffset)
                            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                            .border(
                                2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(
                                    localWindowInfo.current.buttonRounding
                                )
                            )
                            .padding(2.dp)
                    ){
                        if(dialogState.value != null)
                        itemsIndexed((dialogState.value!!.type as DialogPromptType.LIST_SELECTION).list, key={i, item -> item.hashCode()}){ i, item ->
                            var clicked by remember {mutableStateOf(false)}
                            Box(
                                modifier = Modifier
                                    .height(localWindowInfo.current.dialogListItemHeight)
                                    .fillMaxWidth()
                                    .clickable {
                                        clicked = true
                                        (dialogState.value!!.type as DialogPromptType.LIST_SELECTION).selectItem(
                                            item
                                        )
                                        AnimateState.setAnimateState(
                                            animateState.copy(
                                                overlayVisibility = 0f
                                            )
                                        )
                                        lifecycle.launch {
                                            shown.value = false
                                            delay(DIALOG_FUNCTION_DELAY)
                                            DialogPrompt.sendDialog(
                                                null
                                            )
                                        }
                                    }
                                    .clip(
                                        RoundedCornerShape(
                                            topStart =
                                            if (i == 0) localWindowInfo.current.buttonRounding.dp
                                            else 0.dp,
                                            topEnd =
                                            if (i == 0) localWindowInfo.current.buttonRounding.dp
                                            else 0.dp,
                                            bottomStart =
                                            if (i == listSize - 1) localWindowInfo.current.buttonRounding.dp
                                            else 0.dp,
                                            bottomEnd =
                                            if (i == listSize - 1) localWindowInfo.current.buttonRounding.dp
                                            else 0.dp,
                                        )
                                    )
                                    .background(if (!clicked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary)
                            ){
                                Text(
                                    text = item.toString(),
                                    color = if(clicked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                    style = localWindowInfo.current.buttonTextStyle,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                } else if(dialogState.value!!.type is DialogPromptType.FIELD_SELECTION){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(localWindowInfo.current.closeOffset)
                    ){
                        FieldSelection(
                            inputValue = typedText.value,
                            onInputChange = {typedText.value = it},
                            buttonColor = MaterialTheme.colorScheme.secondary,
                            textColor = MaterialTheme.colorScheme.onSecondary,
                            noLabel = true
                        )
                    }
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(windowInfo.getMinTimes(0.35f))
                        .padding(windowInfo.getMinTimes(0.02f))
                ) {
                    if (dialogState.value!!.type is DialogPromptType.CONFIRMATION || dialogState.value!!.type is DialogPromptType.FIELD_SELECTION) {
                        repeat(2) {
                            Box(Modifier
                                .fillMaxWidth(0.485f)
                                .fillMaxHeight()
                                .align(
                                    if (it == 0) Alignment.CenterStart
                                    else Alignment.CenterEnd
                                )
                                .clip(
                                    RoundedCornerShape(
                                        windowInfo.getMinTimes(
                                            0.035f
                                        )
                                    )
                                )
                                .clickable {
                                    if (it == 1) {
                                        if (dialogState.value!!.type == DialogPromptType.FIELD_SELECTION) {
                                            dialogState.value!!.function(
                                                typedText.value
                                            )
                                        } else {
                                            dialogState.value!!.function(
                                                ""
                                            )
                                        }
                                    }
                                    AnimateState.setAnimateState(
                                        animateState.copy(
                                            overlayVisibility = 0f
                                        )
                                    )
                                    lifecycle.launch {
                                        shown.value = false
                                        delay(DIALOG_FUNCTION_DELAY)
                                        DialogPrompt.sendDialog(
                                            null
                                        )
                                    }
                                }
                                .padding(
                                    windowInfo.getMinTimes(
                                        0.01f
                                    )
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (it == 0) "Cancel" else dialogState.value!!.confirmLabel,
                                    style = localWindowInfo.current.buttonTextStyle,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    else if (dialogState.value!!.type is DialogPromptType.NOTICE) {
                        Box(Modifier
                            .fillMaxSize()
                            .fillMaxHeight()
                            .align(Alignment.Center)
                            .clip(
                                RoundedCornerShape(
                                    windowInfo.getMinTimes(
                                        0.035f
                                    )
                                )
                            )
                            .clickable {
                                dialogState.value!!.function(
                                    ""
                                )
                                AnimateState.setAnimateState(
                                    animateState.copy(
                                        overlayVisibility = 0f
                                    )
                                )
                                lifecycle.launch {
                                    shown.value = false
                                    delay(DIALOG_FUNCTION_DELAY)
                                    DialogPrompt.sendDialog(
                                        null
                                    )
                                }
                            }
                            .padding(
                                windowInfo.getMinTimes(
                                    0.0325f
                                )
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dialogState.value!!.confirmLabel,
                                style = moduleLabel + TextStyle(
                                    color =
                                    MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center,
                                    fontSize = windowInfo.getMinTimes(
                                        0.04f
                                    ).value.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}