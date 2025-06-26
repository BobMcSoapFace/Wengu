package com.languageApp.wengu.ui.composables.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.data.DataAction
import com.languageApp.wengu.data.DataEntry
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.modules.SnackbarEvent
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo
import com.languageApp.wengu.ui.theme.DebugState
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun VocabScreen(
    vocabList : State<List<Vocab>>,
    getTestResults : suspend (DataEntry) -> List<TestResult>,
    modifier : Modifier = Modifier,
    navigateTo : (String) -> Unit,
    editingVocabState : MutableState<Vocab?>
){
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(localWindowInfo.current.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(vocabList.value, key = { it.id }) { vocab ->
                var clicked by remember { mutableStateOf(false) }
                val size by animateDpAsState(
                    targetValue = if (clicked) localWindowInfo.current.buttonAnimateSize.times(0.5f) else localWindowInfo.current.buttonAnimateSize,
                    animationSpec = tween(
                        durationMillis = if (clicked) 10 else 100,
                        easing = LinearOutSlowInEasing
                    ), label = "${vocab.vocab} vocab button text color",
                    finishedListener = { clicked = false }
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = localWindowInfo.current.columnItemOffset)
                        .padding(size)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .align(Alignment.TopCenter)
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
                        text = vocab.translation,//LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(vocab.dateCreated), ZoneId.systemDefault()).toString().split("T")[0],
                        textAlign = TextAlign.Right,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = localWindowInfo.current.buttonTextStyle,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.4f)
                            .align(Alignment.BottomEnd)
                    )
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