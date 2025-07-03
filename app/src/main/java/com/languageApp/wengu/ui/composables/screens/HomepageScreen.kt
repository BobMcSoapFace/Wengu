package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.languageApp.wengu.data.DataAction
import com.languageApp.wengu.data.DataEntry
import com.languageApp.wengu.data.Test
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.data.settings.UserSettingsData
import com.languageApp.wengu.modules.DebugHelper.Companion.generateTests
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.modules.DialogPromptType
import com.languageApp.wengu.ui.AnimateState
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.composables.units.NavigationBar
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch

enum class HomepageState {
    HOMEPAGE,
    VOCAB,
    TESTS,
    SETTINGS,
}
@Composable
fun HomepageScreen(
    vocabList : State<List<Vocab>>,
    testList : State<List<Test>>,
    onDataAction : (DataAction) -> Unit,
    getTestResults : suspend (DataEntry) -> List<TestResult>,
    navigateTo : (String) -> Unit,
    navigateBack : () -> Unit,
    editingVocabState : MutableState<Vocab?>,
    viewingVocabState : MutableState<Vocab?>,
    userSettingsData: UserSettingsData,
){
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    var homepageState by rememberSaveable {
        mutableStateOf(HomepageState.HOMEPAGE)
    }
    val testScope = rememberCoroutineScope()
    val animateState = AnimateState.localAnimateState.current
    val navigateables : List<InteractableIcon> = remember {
        listOf(
            InteractableIcon({
                homepageState = HomepageState.HOMEPAGE
            }, "Home", primary, onPrimary, icon=Icons.Default.Home),
            InteractableIcon({
                homepageState = HomepageState.VOCAB
            }, "Vocab", primary, onPrimary, icon=Icons.Default.Star),
            InteractableIcon({
                homepageState = HomepageState.TESTS
                testScope.launch {
                    DialogPrompt.sendDialog(
                        DialogPrompt(
                            type = DialogPromptType.CONFIRMATION,
                            function = {
                                generateTests().forEach {
                                    onDataAction(DataAction.Upsert(it))
                                }
                            },
                            message = "Generate tests?",
                            confirmLabel = "Generate"
                        )
                    )
                }
            }, "Tests", primary, onPrimary, icon=Icons.Default.Email),
            InteractableIcon({
                homepageState = HomepageState.SETTINGS
            }, "Settings", primary, onPrimary, icon=Icons.Default.MoreVert)
        )
    }
    Box(modifier = Modifier
        .fillMaxSize()
    ){
        when(homepageState){
            HomepageState.HOMEPAGE -> {}
            HomepageState.TESTS -> {
                TestViewerScreen(
                    navigateUp = navigateBack,
                    navigateTo = navigateTo,
                    tests = testList,
                    getTestResults = getTestResults,
                )
            }
            HomepageState.VOCAB -> {
                VocabScreen(
                    vocabList = vocabList,
                    navigateTo = navigateTo,
                    editingVocabState = editingVocabState,
                    viewingVocabState = viewingVocabState,
                    onDataAction = onDataAction
                )
            }
            HomepageState.SETTINGS -> {
                SettingsScreen(
                    navigateBack = navigateBack,
                    userSettingsData = userSettingsData
                )
            }
        }
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(localWindowInfo.current.navigateBarHeight),
            buttons = navigateables,
        )
    }
}
