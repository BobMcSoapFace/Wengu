package com.languageApp.wengu.ui.composables.screens

import androidx.collection.mutableIntSetOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.languageApp.wengu.data.Test
import com.languageApp.wengu.data.TestQuestion
import com.languageApp.wengu.data.TestState
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.data.settings.UserSettings
import com.languageApp.wengu.modules.SnackbarEvent
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.Divider
import com.languageApp.wengu.ui.composables.units.buttons.CounterButton
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.composables.units.buttons.ListSelection
import com.languageApp.wengu.ui.composables.units.buttons.SelectionButton
import com.languageApp.wengu.ui.composables.units.buttons.ToggleButton
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch

@Composable
fun TestCreatorScreen(
    vocab : State<List<Vocab>>,
    navigateTo : (String) -> Unit,
    tests : State<List<Test>>,
    navigateUp : () -> Unit,
    activeTest : MutableState<TestState?>
){
    val userSettings = UserSettings.localSettings.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val languageType = rememberSaveable { mutableStateOf(userSettings.defaultLanguage) }
    val languages = remember { mutableStateOf(userSettings.languages.split(Vocab.LANGUAGES_DELIMITER)) }
    val numQuestions = rememberSaveable { mutableIntStateOf(userSettings.defaultQuestionCount) }
    val quizRecentVocab = rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.navigateBarHeight)
                .align(Alignment.TopCenter)
                .background(MaterialTheme.colorScheme.surface)
                .padding(localWindowInfo.current.closeOffset)

        ){
            Text(
                text = "New Test",
                color = MaterialTheme.colorScheme.onSurface,
                style = localWindowInfo.current.titleTextStyle,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
            )
        }
        Column(
            modifier = Modifier
                .offset(y = localWindowInfo.current.navigateBarHeight)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(localWindowInfo.current.screenPadding)
        ){
            SelectionButton(
                item = languageType,
                optionList = languages,
                label = "Language",
                onSelect = {
                    languageType.value = it
                },
                buttonColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
            )
            Divider()
            ToggleButton(
                label = "Recent vocab",
                boolValue = quizRecentVocab,
                onClick = { quizRecentVocab.value = !quizRecentVocab.value },
                buttonColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(localWindowInfo.current.longButtonHeight)
                    .align(Alignment.CenterHorizontally),
                border = true,
            )
            Divider()
            CounterButton(
                label = "Questions",
                counter = numQuestions,
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.7f)
                    .height(localWindowInfo.current.textFieldHeight),
                border = MaterialTheme.colorScheme.surface
            )
            Divider(4)
        }
        IconButton(
            iconInteractableIcon = InteractableIcon(
                function = {
                    val applicableVocabs = vocab.value.filter { it.vocabLanguage == languageType.value }
                    if(numQuestions.intValue > applicableVocabs.size){
                        coroutineScope.launch {
                            SnackbarEvent.sendSnackbarEvent(
                                SnackbarEvent("Not enough vocabulary for ${numQuestions.intValue} questions (${applicableVocabs.size})")
                            )
                        }
                        return@InteractableIcon
                    }
                    if(applicableVocabs.size>=TestQuestion.MultipleChoice.MULTIPLE_CHOICE_NUM){
                        activeTest.value = TestState(
                            numQuestions = numQuestions.intValue,
                            vocabs = applicableVocabs,
                            language = languageType.value,
                            useRecent = quizRecentVocab.value,
                            testIndex = Test.getNextIndex(tests.value),
                        )
                        navigateTo("ActiveTest")
                    } else {
                        coroutineScope.launch {
                            SnackbarEvent.sendSnackbarEvent(
                                SnackbarEvent("Not enough vocabulary for a quiz (${applicableVocabs.size}/${TestQuestion.MultipleChoice.MULTIPLE_CHOICE_NUM})")
                            )
                        }
                    }
                },
                label = "Start",
                icon = Icons.Default.PlayArrow,
                color = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onTextColor = MaterialTheme.colorScheme.primary
            ),
            buttonColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .width(localWindowInfo.current.landscapeButtonWidth)
                .height(localWindowInfo.current.landscapeButtonHeight)
                .align(Alignment.BottomEnd)
                .offset(
                    x = -localWindowInfo.current.screenPadding,
                    y = -(localWindowInfo.current.screenPadding + localWindowInfo.current.navigateBarHeight),
                ),
            expanded = localWindowInfo.current.screenHeightInfo != WindowInfo.WindowType.Compact,
            haveBackgroundBorder = MaterialTheme.colorScheme.onSurface
        )
    }
}