package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.languageApp.wengu.data.DataEntry
import com.languageApp.wengu.data.Test
import com.languageApp.wengu.data.TestQuestion
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.data.settings.UserSettings
import com.languageApp.wengu.modules.DebugHelper
import com.languageApp.wengu.ui.composables.units.Divider
import com.languageApp.wengu.ui.composables.units.TestCard
import com.languageApp.wengu.ui.composables.units.VocabCard
import com.languageApp.wengu.ui.composables.units.buttons.ToggleButton
import com.languageApp.wengu.ui.localWindowInfo

@Composable
fun HomeScreen(
    tests : State<List<Test>>,
    vocab : State<List<Vocab>>,
    testResults : State<List<TestResult>>,
    getTestResults : suspend (DataEntry) -> List<TestResult>,
    navigateTo : (String) -> Unit,
    changeScreen : (HomepageState) -> Unit,
){
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor = when(UserSettings.localSettings.current.useDarkMode){
        1 -> MaterialTheme.colorScheme.background
        0 -> MaterialTheme.colorScheme.secondary
        else ->
            if(isSystemInDarkTheme())
                MaterialTheme.colorScheme.background
            else
                MaterialTheme.colorScheme.secondary
    }
    val textColor = when(UserSettings.localSettings.current.useDarkMode){
        1 -> MaterialTheme.colorScheme.onBackground
        0 -> MaterialTheme.colorScheme.onSecondary
        else ->
            if(isSystemInDarkTheme())
                MaterialTheme.colorScheme.onBackground
            else
                MaterialTheme.colorScheme.onSecondary
    }
    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
            , horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(localWindowInfo.current.screenHeight.times(0.5f))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(localWindowInfo.current.closeOffset)
                , verticalArrangement = Arrangement.Center
                , horizontalAlignment = Alignment.CenterHorizontally
            ){
                when {
                    vocab.value.size < TestQuestion.MultipleChoice.MULTIPLE_CHOICE_NUM -> {
                        Text(
                            text = if(vocab.value.isEmpty()) "No vocab created yet. Create one now!"
                            else "Create more vocab!",
                            style = localWindowInfo.current.titleTextStyle,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(localWindowInfo.current.slightOffset)
                        )
                        Divider(3)
                        ToggleButton(
                            label = "Go",
                            boolValue = remember { mutableStateOf(false) },
                            onClick = { changeScreen(HomepageState.VOCAB) },
                            border = true
                        )
                    }
                    tests.value.isEmpty() -> {
                        Text(
                            text = "Take your first test!",
                            style = localWindowInfo.current.titleTextStyle,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(localWindowInfo.current.slightOffset)
                        )
                        Divider(3)
                        ToggleButton(
                            label = "Go",
                            boolValue = remember { mutableStateOf(false) },
                            onClick = { changeScreen(HomepageState.TESTS) },
                            border = true
                        )
                    }
                    testResults.value.isNotEmpty() && !vocab.value.none{ Vocab.getVocabPercent(it, testResults.value) < 0.7f} && DebugHelper.RANDOM.nextBoolean() -> {
                        val worstVocab = remember { TestResult.getWorstVocab(vocab.value, testResults.value) }
                        val worstVocabPercent = remember { TestResult.getVocabPercent(worstVocab, testResults.value) }
                        Text(
                            text = "You have a score of ${String.format("%.1f", worstVocabPercent*100)}% on ${worstVocab.vocab}. Practice it again?",
                            style = localWindowInfo.current.titleTextStyle,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(localWindowInfo.current.slightOffset)
                        )
                        Divider(3)
                        ToggleButton(
                            label =
                                if(worstVocabPercent < 0.5f && DebugHelper.RANDOM.nextInt()%10==0) "Restore honor"
                                else "Go",
                            boolValue = remember { mutableStateOf(false) },
                            onClick = { changeScreen(HomepageState.TESTS) },
                            border = true
                        )
                    }
                    else -> {
                        Text(
                            text =
                                if(DebugHelper.RANDOM.nextInt()%30==0) "\uD83D\uDC3C".repeat(7)
                                else "Take another test?",
                            style = localWindowInfo.current.titleTextStyle,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(localWindowInfo.current.slightOffset)
                        )
                        Divider(3)
                        ToggleButton(
                            label =
                                if(DebugHelper.RANDOM.nextInt()%20==0) "I love tests"
                                else if(DebugHelper.RANDOM.nextInt()%15==0) "Fine"
                                else "Go",
                            boolValue = remember { mutableStateOf(false) },
                            onClick = { changeScreen(HomepageState.TESTS) },
                            border = true
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(localWindowInfo.current.screenHeight.times(0.05f))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .clip(
                        RoundedCornerShape(
                            topStartPercent = 100,
                            topEndPercent = 100
                        )
                    )
                    .background(backgroundColor)
            )
            if(tests.value.isNotEmpty()){
                val bestTest = remember {
                    Test.getBestTest(tests.value, testResults.value)
                }
                if(bestTest!=null){
                    Text(
                        text = "Your best test",
                        style = localWindowInfo.current.titleTextStyle,
                        color = textColor,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                            .padding(localWindowInfo.current.slightOffset)
                    )
                    TestCard(
                        test = bestTest,
                        getTestResults = getTestResults,
                        getTestResultsScope = coroutineScope
                    )
                    Divider(2)
                }
            }
            if(vocab.value.isNotEmpty() && testResults.value.isNotEmpty()){
                val bestVocab = remember {
                    Vocab.getBestVocab(vocab.value, testResults.value)
                }
                if(bestVocab!=null){
                    Text(
                        text = "Your best vocab",
                        style = localWindowInfo.current.titleTextStyle,
                        color = textColor,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                            .padding(localWindowInfo.current.slightOffset)
                    )
                    Text(
                        text = String.format("%.1f", Vocab.getVocabPercent(
                            vocab = bestVocab,
                            results = testResults.value
                        )*100) + "%",
                        style = localWindowInfo.current.titleTextStyle,
                        color = textColor,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                            .padding(localWindowInfo.current.slightOffset)
                    )
                    VocabCard(
                        vocab = bestVocab,
                        modifier = Modifier.padding(horizontal = localWindowInfo.current.screenPadding)
                    )
                }
            }
            Divider(6)
        }
    }
}