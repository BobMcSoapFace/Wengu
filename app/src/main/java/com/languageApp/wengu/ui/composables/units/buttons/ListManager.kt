package com.languageApp.wengu.ui.composables.units.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.modules.DialogPromptType
import com.languageApp.wengu.ui.AnimateState
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch

@Composable
inline fun <reified T>ListManager(
    list : State<List<T>>,
    crossinline add : (String) -> Unit,
    crossinline delete : (T) -> Unit,
    modifier : Modifier = Modifier,
    label : String = "Label",
    dialogMessage : String = "Add a new ${label}",
    buttonColor : Color = MaterialTheme.colorScheme.primary,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    listColor : Color = textColor,
    listTextColor : Color = buttonColor,
){
    Column(
        modifier = modifier
            .padding(localWindowInfo.current.buttonAnimateSize)
            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
            .background(buttonColor)
            .padding(localWindowInfo.current.closeOffset)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
            , contentAlignment = Alignment.TopCenter
        ){
            Text(
                text= label,
                color = textColor,
                style = localWindowInfo.current.fieldLabeltextStyle,
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(localWindowInfo.current.closeOffset)
                    .align(Alignment.Center)
            )
        }
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
            itemsIndexed(list.value, key={ _, item -> item.hashCode()}){ i, item ->
                Box(
                    modifier = Modifier
                        .height(localWindowInfo.current.dialogListItemHeight)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topStart =
                                if (i == 0) localWindowInfo.current.buttonRounding.dp
                                else 0.dp,
                                topEnd =
                                if (i == 0) localWindowInfo.current.buttonRounding.dp
                                else 0.dp,
                                bottomStart =
                                if (i == list.value.size - 1) localWindowInfo.current.buttonRounding.dp
                                else 0.dp,
                                bottomEnd =
                                if (i == list.value.size - 1) localWindowInfo.current.buttonRounding.dp
                                else 0.dp,
                            )
                        )
                        .background(listColor)
                        .padding(localWindowInfo.current.slightOffset)
                    ,contentAlignment = Alignment.Center,
                ){
                    Text(
                        text = item.toString(),
                        color = listTextColor,
                        style = localWindowInfo.current.buttonTextStyle,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete ${item.toString()} button",
                        tint = listTextColor,
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .aspectRatio(1f, true)
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                            .clickable {
                                delete(item)
                            }
                    )
                }
            }
            item{
                val addScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .height(localWindowInfo.current.dialogListItemHeight)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding.dp))
                        .background(listColor)
                        .clickable {
                            addScope.launch {
                                DialogPrompt.sendDialog(
                                    DialogPrompt(
                                        message = dialogMessage,
                                        function = { add(it) },
                                        type = DialogPromptType.FIELD_SELECTION
                                    )
                                )
                            }
                        }
                        .padding(localWindowInfo.current.slightOffset)
                    ,contentAlignment = Alignment.Center,
                ){
                    Text(
                        text = "Add +",
                        color = listTextColor,
                        style = localWindowInfo.current.buttonTextStyle,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}