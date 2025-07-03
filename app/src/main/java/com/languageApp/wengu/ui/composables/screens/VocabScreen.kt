package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.languageApp.wengu.data.DataAction
import com.languageApp.wengu.modules.DebugHelper
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.modules.DialogPromptType
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch

@Composable
fun VocabScreen(
    vocabList : State<List<Vocab>>,
    modifier : Modifier = Modifier,
    navigateTo : (String) -> Unit,
    editingVocabState : MutableState<Vocab?>,
    viewingVocabState : MutableState<Vocab?>,
    onDataAction : (DataAction) -> Unit,
){
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()){
        if(vocabList.value.isNotEmpty())
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(localWindowInfo.current.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(vocabList.value, key = { it.id }) { vocab ->
                Box(
                    modifier = Modifier
                        .padding(vertical = localWindowInfo.current.columnItemOffset)
                        .padding(localWindowInfo.current.buttonAnimateSize)
                        .fillMaxWidth()
                        .height(localWindowInfo.current.vocabItemHeight)
                        .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            editingVocabState.value = vocab
                            navigateTo("AddVocab")
                        }
                        .padding(localWindowInfo.current.closeOffset)
                ) {
                    Text(
                        text = vocab.vocab,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = localWindowInfo.current.vocabTextStyle,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth(0.675f)
                            .fillMaxHeight(0.6f)
                            .align(Alignment.TopStart)
                    )
                    Text(
                        text = vocab.pronunciation,
                        textAlign = TextAlign.Left,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = localWindowInfo.current.buttonTextStyle,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.4f)
                            .align(Alignment.BottomStart)
                    )
                    Text(
                        text = vocab.translation,
                        textAlign = TextAlign.Right,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = localWindowInfo.current.buttonTextStyle,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.4f)
                            .align(Alignment.BottomEnd)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .fillMaxHeight(0.45f)
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable {
                                viewingVocabState.value = vocab
                                navigateTo("VocabResults")
                            }
                            .padding(localWindowInfo.current.slightOffset)
                    ){
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = vocab.vocab + " test results icon",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f, true)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(localWindowInfo.current.vocabItemHeight)
                )
            }
        }
        else
            Text(
                text = "No vocabs created yet.",
                color = MaterialTheme.colorScheme.onTertiary,
                style = localWindowInfo.current.footnoteTextStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
        IconButton(
            iconInteractableIcon = InteractableIcon(
                function = {
                    editingVocabState.value = null
                    navigateTo("AddVocab")
                },
                label = "New",
                icon = Icons.Default.Add,
                color = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onTextColor = MaterialTheme.colorScheme.primary
            ),
            buttonColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.onSecondary,
            onTextColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(localWindowInfo.current.landscapeButtonWidth)
                .height(localWindowInfo.current.landscapeButtonHeight)
                .align(Alignment.BottomEnd)
                .offset(
                    x = -localWindowInfo.current.screenPadding,
                    y = -(localWindowInfo.current.screenPadding + localWindowInfo.current.navigateBarHeight),
                ),
            expanded = localWindowInfo.current.screenHeightInfo != WindowInfo.WindowType.Compact,
            haveBackgroundBorder = MaterialTheme.colorScheme.primary
        )
    }
}