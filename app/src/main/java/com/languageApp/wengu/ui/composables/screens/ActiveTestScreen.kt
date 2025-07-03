package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.data.TestQuestion
import com.languageApp.wengu.data.TestState
import com.languageApp.wengu.modules.SnackbarEvent
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.Divider
import com.languageApp.wengu.ui.composables.units.Separator
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.composables.units.buttons.ToggleButton
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActiveTestScreen(
    testState : TestState
){
    val pagingState = rememberPagerState(initialPage = 0) { testState.numQuestions }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .height(localWindowInfo.current.screenHeight - localWindowInfo.current.navigateBarHeight)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset(y = localWindowInfo.current.navigateBarHeight)
                .padding(localWindowInfo.current.closeOffset)
        ){
            HorizontalPager(
                state = pagingState,
                pageSpacing = localWindowInfo.current.getMinTimes(0.1f),
                key = {it},
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val testQuestion = remember { mutableStateOf(testState.questions[it]) }
                when(testQuestion.value){
                    is TestQuestion.TrueFalse -> {
                        Text(
                            text = "TrueFalse Question ",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = localWindowInfo.current.footnoteTextStyle,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is TestQuestion.MultipleChoice -> {
                        val verticalScrollState = rememberScrollState()
                        val curAnswerSelected = remember { mutableStateOf((testQuestion.value as TestQuestion.MultipleChoice).entry) }
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = testQuestion.value.vocab.vocab,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = localWindowInfo.current.quizTextStyle,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(localWindowInfo.current.screenPadding)
                            )
                            Text(
                                text = when (testQuestion.value.type) {
                                    TestQuestion.AnswerType.TRANSLATION -> "translation"
                                    TestQuestion.AnswerType.PRONUNCIATION -> "pronunciation"
                                },
                                color = MaterialTheme.colorScheme.onBackground,
                                style = localWindowInfo.current.footnoteTextStyle,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(localWindowInfo.current.screenPadding)
                            )
                            Divider()
                            Separator()
                            Divider()
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .verticalScroll(verticalScrollState)
                                , horizontalArrangement = Arrangement.Center
                            ) {
                                (testQuestion.value as TestQuestion.MultipleChoice).answers.forEach {vocab ->
                                    val choice = remember {
                                        when (testQuestion.value.type) {
                                            TestQuestion.AnswerType.TRANSLATION -> vocab.translation
                                            TestQuestion.AnswerType.PRONUNCIATION -> vocab.pronunciation
                                        }
                                    }
                                    val isSelected = remember {
                                        mutableStateOf(curAnswerSelected.value == choice)
                                    }
                                    LaunchedEffect(key1 = curAnswerSelected.value) {
                                        isSelected.value = curAnswerSelected.value == choice
                                    }

                                    ToggleButton(
                                        label = choice,
                                        boolValue = isSelected,
                                        onClick = {
                                            curAnswerSelected.value = choice
                                            (testQuestion.value as TestQuestion.MultipleChoice).entry = choice
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(0.45f)
                                            .height(localWindowInfo.current.quizButtonHeight)
                                            .padding(localWindowInfo.current.closeOffset),
                                        border = true
                                    )
                                }
                            }
                            Divider(4)
                        }
                    }
                    else -> {
                        Text(
                            text = "Question ${it+1} could not be resolved. ",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = localWindowInfo.current.footnoteTextStyle,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
        if(pagingState.currentPage >= pagingState.pageCount-1){
            IconButton(
                iconInteractableIcon = InteractableIcon(
                    function = {

                    },
                    label = "Submit",
                    icon = Icons.Default.PlayArrow,
                    color = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    onTextColor = MaterialTheme.colorScheme.primary
                ),
                buttonColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .width(localWindowInfo.current.landscapeButtonWidth)
                    .height(localWindowInfo.current.landscapeButtonHeight)
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = -localWindowInfo.current.screenPadding,
                        y = -(localWindowInfo.current.screenPadding + localWindowInfo.current.navigateBarHeight),
                    ),
                expanded = localWindowInfo.current.screenHeightInfo != WindowInfo.WindowType.Compact,
                haveBackgroundBorder = MaterialTheme.colorScheme.onSecondary,
                showText = true,
            )
        }

        // question progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.navigateBarHeight)
                .padding(localWindowInfo.current.closeOffset)
                .align(Alignment.TopCenter)
            , contentAlignment = Alignment.Center
        ){
            Text(
                text = "${pagingState.currentPage+1} / ${testState.numQuestions}",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                style = localWindowInfo.current.titleTextStyle
            )
        }
    }
}