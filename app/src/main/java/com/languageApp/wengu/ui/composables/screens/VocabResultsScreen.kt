package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.data.Sort
import com.languageApp.wengu.data.SortType
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.composables.units.Divider
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo
import com.languageApp.wengu.ui.theme.ColorState
import java.time.LocalDateTime
import java.time.ZoneId


@Composable
fun VocabResultsScreen(
    navigateBack : () -> Unit,
    selectedVocab : Vocab?,
    testResults : State<List<TestResult>>
){
    val sort = Sort.localSort.current
    val testResultItems = remember {
        if(selectedVocab != null) testResults.value
            .filter{ it.vocabId == selectedVocab.id }
            .sortedByDescending { when(sort.testResultSortType){
                SortType.TestResult.BY_TEST -> return@sortedByDescending it.testId.toLong()
                SortType.TestResult.SECONDS_TAKEN -> return@sortedByDescending  it.secondsTaken.toLong()
                else -> return@sortedByDescending it.dateTaken
            } }
        else listOf()
    }
    val numCorrect = remember { testResultItems.filter{ it.correct }.size}
    val percentCorrect = remember { if(testResultItems.isNotEmpty()) numCorrect.toFloat()/testResultItems.size else 0.0f }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.navigateBarHeight)
                .align(Alignment.TopCenter)
                .background(MaterialTheme.colorScheme.surface)
                .padding(localWindowInfo.current.closeOffset)
            , contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = selectedVocab?.vocab ?: "null",
                style = localWindowInfo.current.titleTextStyle,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.navigateBarHeight)
                .align(Alignment.TopCenter)
                .offset(y= localWindowInfo.current.navigateBarHeight+1.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(localWindowInfo.current.closeOffset)
            , contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = "${numCorrect}/${testResultItems.size}   ${String.format("%.1f", percentCorrect*100)}%" ?: "null",
                style = localWindowInfo.current.titleTextStyle,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        if(selectedVocab != null && testResultItems.isNotEmpty())
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.screenHeight - localWindowInfo.current.navigateBarHeight)
                .offset(y = localWindowInfo.current.navigateBarHeight*2+1.dp)
                .padding(localWindowInfo.current.screenPadding)
        ){
            items(testResultItems, key= {it.id}){result ->
                Box(
                    modifier = Modifier
                        .padding(vertical = localWindowInfo.current.columnItemOffset)
                        .padding(localWindowInfo.current.buttonAnimateSize)
                        .fillMaxWidth()
                        .height(localWindowInfo.current.vocabItemHeight)
                        .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(localWindowInfo.current.closeOffset)
                ) {
                    Text(
                        text = LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(result.dateTaken), ZoneId.systemDefault()).toString().split("T")[0],
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = localWindowInfo.current.vocabTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .align(Alignment.TopCenter)
                    )
                    Text(
                        text = LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(result.dateTaken), ZoneId.systemDefault()).toString().split("T")[1],
                        textAlign = TextAlign.Left,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = localWindowInfo.current.buttonTextStyle,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.4f)
                            .align(Alignment.BottomStart)
                    )
                    Text(
                        text = if(result.correct) "Correct" else "Incorrect",
                        textAlign = TextAlign.Right,
                        overflow = TextOverflow.Ellipsis,
                        color = if(result.correct) ColorState.SUCCESS.color else ColorState.FAILURE.color,
                        style = localWindowInfo.current.buttonTextStyle,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.4f)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            item{
                Divider(4)
            }
        }
        else
            Text(
                text = "No test results found.",
                color = MaterialTheme.colorScheme.onTertiary,
                style = localWindowInfo.current.footnoteTextStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
        IconButton(
            iconInteractableIcon = InteractableIcon(
                function = {navigateBack()},
                label = "",
                icon = Icons.Default.ArrowDropDown,
                color = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                onTextColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .width(localWindowInfo.current.landscapeButtonHeight)
                .height(localWindowInfo.current.landscapeButtonHeight)
                .align(Alignment.BottomStart)
                .offset(
                    x = localWindowInfo.current.screenPadding,
                    y = -localWindowInfo.current.screenPadding,
                ),
            buttonColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            onTextColor = MaterialTheme.colorScheme.surface,
            expanded = true,
            showText = false,
            haveBackgroundBorder = MaterialTheme.colorScheme.onSurface
        )
    }
}