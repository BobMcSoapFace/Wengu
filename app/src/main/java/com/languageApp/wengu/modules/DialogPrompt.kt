package com.languageApp.wengu.modules

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.theme.descriptionText
import com.languageApp.wengu.ui.theme.moduleLabel
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
    }
}
enum class DialogPromptType {
    CONFIRMATION,
    NOTICE,
}

@Composable
fun DialogComposable(
    windowInfo: WindowInfo,
    dialogState: State<DialogPrompt?>,
    lifecycle: LifecycleCoroutineScope,
){
    Box(modifier =
        Modifier.fillMaxSize()
        .background(Color.Transparent)
    ){
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
            horizontalAlignment = Alignment.CenterHorizontally
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(windowInfo.getMinTimes(0.25f))
                    .padding(windowInfo.getMinTimes(0.02f))
            ) {
                when (dialogState.value!!.type) {
                    DialogPromptType.CONFIRMATION -> {
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
                                        dialogState.value!!.function(
                                            ""
                                        )
                                    }
                                    lifecycle.launch {
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
                                    text = if (it == 0) "Cancel" else dialogState.value!!.confirmLabel,
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

                    else -> {
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
                                lifecycle.launch {
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