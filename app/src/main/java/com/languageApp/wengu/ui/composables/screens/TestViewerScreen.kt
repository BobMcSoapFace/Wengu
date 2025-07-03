package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.languageApp.wengu.data.DataEntry
import com.languageApp.wengu.data.Test
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.Divider
import com.languageApp.wengu.ui.composables.units.ScreenPaddingDivider
import com.languageApp.wengu.ui.composables.units.Separator
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo
import com.languageApp.wengu.ui.theme.ColorState
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TestViewerScreen(
    navigateUp : () -> Unit,
    navigateTo: (String) -> Unit,
    tests : State<List<Test>>,
    getTestResults : suspend (DataEntry) -> List<TestResult>
){
    val getTestResultsScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        if(tests.value.isNotEmpty())
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                ScreenPaddingDivider()
            }
            items(items=tests.value, key = {it.id}){test ->
                val associatedTestResults = remember {
                    mutableStateListOf<TestResult>()
                }
                val timeTaken = remember {
                    mutableIntStateOf(0)
                }
                val longestStreak = remember {
                    mutableIntStateOf(0)
                }
                val percentCorrect = remember {
                    mutableFloatStateOf(0f)
                }
                LaunchedEffect(key1 = true) {
                    getTestResultsScope.launch {
                        associatedTestResults.addAll(getTestResults(test).sortedBy{ it.dateTaken })
                        timeTaken.intValue = associatedTestResults.sumOf { it.secondsTaken }
                        percentCorrect.value = (associatedTestResults.filter{it.correct}.size.toFloat())/(associatedTestResults.size)
                        var curRecord = 0
                        associatedTestResults.forEach{ result ->
                            if(result.correct){
                                curRecord++
                                if(curRecord > longestStreak.intValue){
                                    longestStreak.intValue = curRecord
                                }
                            } else {
                                curRecord = 0
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .defaultMinSize(minHeight = localWindowInfo.current.screenHeight.times(0.7f))
                        .height(IntrinsicSize.Max)
                        .padding(vertical = localWindowInfo.current.screenPadding)
                        .clip(RoundedCornerShape(localWindowInfo.current.testRounding))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {

                        }
                        .padding(localWindowInfo.current.screenPadding)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(localWindowInfo.current.navigateBarHeight)
                    ){
                        Text(
                            text = "Test ${test.id}",
                            style = localWindowInfo.current.titleTextStyle,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                        Separator(modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth())
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .padding(localWindowInfo.current.slightOffset)
                    ){
                        Text(
                            text = LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(test.dateTaken), ZoneId.systemDefault()).toString().split("T")[0],
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = localWindowInfo.current.buttonTextStyle,
                            modifier = Modifier
                                .padding(localWindowInfo.current.slightOffset)
                        )
                        Text(
                            text = "${associatedTestResults.filter{it.correct}.size} / ${associatedTestResults.size}",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = localWindowInfo.current.titleTextStyle,
                            modifier = Modifier
                                .padding(localWindowInfo.current.slightOffset)
                        )
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(Alignment.Start)
                                .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                                .background(MaterialTheme.colorScheme.secondary)
                                .padding(localWindowInfo.current.slightOffset)
                        ) {
                            associatedTestResults.forEach{
                                Box(
                                    modifier = Modifier
                                        .width(localWindowInfo.current.testResultIndicatorSize + localWindowInfo.current.testResultIndicatorPadding * 2)
                                        .height(localWindowInfo.current.testResultIndicatorSize + localWindowInfo.current.testResultIndicatorPadding * 2)
                                        .padding(localWindowInfo.current.testResultIndicatorPadding)
                                        .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                                        .background(if (it.correct) ColorState.SUCCESS.color else ColorState.FAILURE.color)
                                )
                            }
                        }
                        Divider()
                        Text(
                            text = test.language,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = localWindowInfo.current.titleTextStyle,
                            modifier = Modifier
                                .padding(localWindowInfo.current.slightOffset)
                        )
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(localWindowInfo.current.slightOffset)){
                            Text(
                                text = "Time taken",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = localWindowInfo.current.footnoteTextStyle,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                            Text(
                                text = if(timeTaken.intValue/60 >= 1)"${timeTaken.intValue/60}m" else "${timeTaken.intValue}s",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = localWindowInfo.current.footnoteTextStyle,
                                textAlign = TextAlign.Right,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(localWindowInfo.current.slightOffset)){
                            Text(
                                text = "Longest streak",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = localWindowInfo.current.footnoteTextStyle,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                            Text(
                                text = longestStreak.intValue.toString(),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = localWindowInfo.current.footnoteTextStyle,
                                textAlign = TextAlign.Right,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(localWindowInfo.current.slightOffset)){
                            Text(
                                text = "Percent correct",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = localWindowInfo.current.footnoteTextStyle,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                            Text(
                                text = "${String.format("%.1f", percentCorrect.floatValue*100)}%",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = localWindowInfo.current.footnoteTextStyle,
                                textAlign = TextAlign.Right,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }
            item{
                ScreenPaddingDivider(4)
            }
        }
        else
            Text(
                text = "No tests taken yet.",
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
                    navigateTo("TestCreator")
                },
                label = "New",
                icon = Icons.Default.Star,
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
            haveBackgroundBorder = MaterialTheme.colorScheme.onPrimary
        )
    }
}