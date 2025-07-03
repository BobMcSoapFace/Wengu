package com.languageApp.wengu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.languageApp.wengu.data.AppDatabase
import com.languageApp.wengu.data.LanguageViewModel
import com.languageApp.wengu.data.Sort
import com.languageApp.wengu.data.settings.UserSettings
import com.languageApp.wengu.modules.DialogComposable
import com.languageApp.wengu.modules.DialogPrompt
import com.languageApp.wengu.modules.SnackbarEvent
import com.languageApp.wengu.ui.AnimateState
import com.languageApp.wengu.ui.Screen
import com.languageApp.wengu.ui.composables.screens.ActiveTestScreen
import com.languageApp.wengu.ui.composables.screens.AddVocabScreen
import com.languageApp.wengu.ui.composables.screens.HomepageScreen
import com.languageApp.wengu.ui.composables.screens.TestCreatorScreen
import com.languageApp.wengu.ui.composables.screens.TestViewerScreen
import com.languageApp.wengu.ui.composables.screens.VocabResultsScreen
import com.languageApp.wengu.ui.composables.units.Overlay
import com.languageApp.wengu.ui.localWindowInfo
import com.languageApp.wengu.ui.rememberWindowInfo
import com.languageApp.wengu.ui.theme.WenguTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            /*
            * ^ use if 1. altering database columns/tables AND 2. don't need data to migrate
            */
            .build()
    }
    private val languageViewModel by viewModels<LanguageViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LanguageViewModel(application, db) as T
                }
            }
        })
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()

            val vocabListState = languageViewModel.vocabState.collectAsStateWithLifecycle()
            val testListState = languageViewModel.testState.collectAsStateWithLifecycle()
            val testResultListState = languageViewModel.testResultsState.collectAsStateWithLifecycle()
            val animationState = languageViewModel.animationState.collectAsStateWithLifecycle()
            val sortTypeState = languageViewModel.sortTypeState.collectAsStateWithLifecycle()
            val dialogState = DialogPrompt.dialogSharedFlow.collectAsStateWithLifecycle(initialValue = null)
            val userSettingsState = languageViewModel.userSettingsState.collectAsStateWithLifecycle(initialValue = UserSettings())

            val setAnimateState : (AnimateState) -> Unit = remember {
                { state: AnimateState ->
                    lifecycleScope.launch { languageViewModel.setAnimateState(state) }
                }
            }
            val navigateTo : (route : String) -> Unit = remember {
                {
                    setAnimateState(animationState.value.copy(overlayVisibility = 0f))
                    navController.navigate(route = it)
                }
            }
            val navigateBack : () -> Unit = remember {
                {
                    setAnimateState(animationState.value.copy(overlayVisibility = 0f))
                    navController.navigateUp()
                }
            }


            LaunchedEffect(key1 = true) {
                AnimateState.setAnimateState = setAnimateState
                lifecycleScope.launch {
                    SnackbarEvent.SnackbarSharedFlow.collect { snackbarEvent ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = snackbarEvent.text,
                                withDismissAction = true,
                                duration = snackbarEvent.time,
                            )
                        }
                    }
                }
            }
            val screens : List<Screen> = listOf(
                Screen(
                    route = "Homepage",
                    screen = {
                        HomepageScreen(
                            vocabList = vocabListState,
                            testList = testListState,
                            getTestResults = languageViewModel::getTestResults,
                            onDataAction = languageViewModel::onDataAction,
                            navigateTo = navigateTo,
                            navigateBack = navigateBack,
                            editingVocabState = languageViewModel.editingVocab,
                            viewingVocabState = languageViewModel.viewingVocab,
                            userSettingsData = languageViewModel.userSettings
                        )
                    }
                ),
                Screen(
                    route = "AddVocab",
                    screen = {
                        AddVocabScreen(
                            vocabList = vocabListState,
                            onDataAction = languageViewModel::onDataAction,
                            navigateTo = navigateTo,
                            navigateBack = navigateBack,
                            editingVocab = languageViewModel.editingVocab.value
                        )
                    }
                ),
                Screen(
                    route = "VocabResults",
                    screen = {
                        VocabResultsScreen(
                            navigateBack = navigateBack,
                            selectedVocab = languageViewModel.viewingVocab.value,
                            testResults = testResultListState
                        )
                    }
                ),
                Screen(
                    route = "TestCreator",
                    screen = {
                        TestCreatorScreen(
                            navigateUp = navigateBack,
                            navigateTo = navigateTo,
                            vocab = vocabListState,
                            activeTest = languageViewModel.activeTestState,
                        )
                    }
                ),
                Screen(
                    route = "ActiveTest",
                    screen = {
                        if(languageViewModel.activeTestState.value!=null) {
                            ActiveTestScreen(
                                testState = languageViewModel.activeTestState.value!!
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize()){
                                Text(
                                    text = "Loading test... ",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = localWindowInfo.current.footnoteTextStyle,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                    }
                ),

            )
            val windowInfo = rememberWindowInfo()
            WenguTheme(darkTheme = when(userSettingsState.value.useDarkMode){
                1 -> true
                0 -> false
                else -> isSystemInDarkTheme()
            }, dynamicColor = false) {
                CompositionLocalProvider(
                    localWindowInfo provides windowInfo,
                    UserSettings.localSettings provides userSettingsState.value,
                    Sort.localSort provides sortTypeState.value,
                    AnimateState.localAnimateState provides animationState.value
                ){
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        SharedTransitionLayout {
                            NavHost(
                                navController = navController, startDestination = screens[0].route,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                screens.forEach { entry ->
                                    composable(
                                        route = entry.route,
                                        //animation for screen transitions
                                        enterTransition = {
                                            slideInVertically(
                                                animationSpec = tween(500)
                                            ) { height ->
                                                2 * (height / 3)
                                            } + fadeIn(animationSpec = tween(800))
                                        },
                                        exitTransition = {
                                            slideOutVertically(
                                                animationSpec = tween(500)
                                            ) { height ->
                                                2 * (height / 3)
                                            } + fadeOut(animationSpec = tween(800))
                                        },
                                    ) {
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.background)
                                        ) {
                                            entry.screen()
                                            Overlay()
                                            //shows dialog if dialog exists
                                            if (dialogState.value != null) {
                                                DialogComposable(
                                                    windowInfo = windowInfo,
                                                    dialogState = dialogState,
                                                    lifecycle = lifecycleScope,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

