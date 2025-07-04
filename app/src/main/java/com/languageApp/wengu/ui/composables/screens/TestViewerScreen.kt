package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.languageApp.wengu.data.DataEntry
import com.languageApp.wengu.data.Sort
import com.languageApp.wengu.data.SortType
import com.languageApp.wengu.data.Test
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.Divider
import com.languageApp.wengu.ui.composables.units.ScreenPaddingDivider
import com.languageApp.wengu.ui.composables.units.Separator
import com.languageApp.wengu.ui.composables.units.TestCard
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo
import com.languageApp.wengu.ui.theme.ColorState
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TestViewerScreen(
    navigateTo: (String) -> Unit,
    tests : State<List<Test>>,
    getTestResults : suspend (DataEntry) -> List<TestResult>
){
    val getTestResultsScope = rememberCoroutineScope()
    val sort = Sort.localSort.current
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
            items(items=
                when(sort.testSortType) {
                    SortType.Test.ID -> tests.value.sortedByDescending { it.id }
                    SortType.Test.DATE -> tests.value.sortedByDescending { it.dateTaken }
                    SortType.Test.LANGUAGE -> tests.value.sortedBy { it.language }
                }
            , key = {it.id}){test ->
                TestCard(
                    test = test,
                    getTestResults = getTestResults,
                    getTestResultsScope = getTestResultsScope
                )
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