package com.languageApp.wengu.ui.composables.screens

import android.widget.ToggleButton
import androidx.collection.mutableIntSetOf
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.data.settings.UserSettings
import com.languageApp.wengu.data.settings.UserSettingsData
import com.languageApp.wengu.ui.composables.units.Divider
import com.languageApp.wengu.ui.composables.units.buttons.CounterButton
import com.languageApp.wengu.ui.composables.units.buttons.ListManager
import com.languageApp.wengu.ui.composables.units.buttons.SelectionButton
import com.languageApp.wengu.ui.composables.units.buttons.ToggleButton
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    navigateBack : () -> Unit,
    userSettingsData: UserSettingsData,
){
    val scrollState = rememberScrollState()
    val saveSettingsScope = rememberCoroutineScope()
    val userSettings = UserSettings.localSettings.current
    val systemDarkTheme = isSystemInDarkTheme()
    val languages = rememberSaveable { mutableStateOf(userSettings.languages.split(Vocab.LANGUAGES_DELIMITER)) }
    val types = rememberSaveable { mutableStateOf(userSettings.types.split(Vocab.TYPE_DELIMITER)) }
    val useDarkMode : MutableState<Boolean?> = rememberSaveable { mutableStateOf(when(userSettings.useDarkMode) {
        1 -> true
        0 -> false
        else -> systemDarkTheme
    }) }
    val defaultLanguage = rememberSaveable {
        mutableStateOf(if(userSettings.defaultLanguage.isNotEmpty()) userSettings.defaultLanguage else languages.value[0])
    }
    val defaultQuestionCount = rememberSaveable {
        mutableIntStateOf(userSettings.defaultQuestionCount)
    }
    LaunchedEffect(key1 = defaultQuestionCount.intValue) {
        saveSettingsScope.launch {
            userSettingsData.saveSettingsData(userSettings.copy(defaultQuestionCount = defaultQuestionCount.intValue))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(localWindowInfo.current.screenPadding)
    ){
        ListManager<String>(
            label = "Languages",
            list = languages as State<List<String>>,
            add = {
                it -> languages.value+=it.trim().lowercase().replaceFirstChar{it.uppercaseChar()}
                saveSettingsScope.launch {
                    userSettingsData.saveSettingsData(userSettings.copy(languages = languages.value.joinToString(Vocab.LANGUAGES_DELIMITER)))
                }
                  },
            delete = {
                languages.value-=it
                saveSettingsScope.launch {
                    userSettingsData.saveSettingsData(userSettings.copy(languages = languages.value.joinToString(Vocab.LANGUAGES_DELIMITER)))
                }
                     },
            modifier = Modifier
                .height(localWindowInfo.current.dialogListHeight.times(1.5f))
                .fillMaxWidth(1f)
        )
        Divider()
        ListManager<String>(
            label = "Vocab Types",
            list = types as State<List<String>>,
            add = {
                it -> types.value+=it.trim().lowercase().replaceFirstChar{it.uppercaseChar()}
                saveSettingsScope.launch {
                    userSettingsData.saveSettingsData(userSettings.copy(types = types.value.joinToString(Vocab.TYPE_DELIMITER)))
                }
                  },
            delete = {
                types.value-=it
                saveSettingsScope.launch {
                    userSettingsData.saveSettingsData(userSettings.copy(types = types.value.joinToString(Vocab.TYPE_DELIMITER)))
                }
                     },
            modifier = Modifier
                .height(localWindowInfo.current.dialogListHeight.times(1.5f))
                .fillMaxWidth(1f)
        )
        Divider()
        ToggleButton(
            label = "Dark display",
            boolValue = useDarkMode as State<Boolean?>,
            onClick = {
                useDarkMode.value = useDarkMode.value!=true
                saveSettingsScope.launch {
                    userSettingsData.saveSettingsData(userSettings.copy(useDarkMode=when(useDarkMode.value) {
                        true -> 1
                        false -> 0
                        null -> -1
                    }))
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.longButtonHeight),
            border = true
        )
        Divider()
        SelectionButton(
            item = defaultLanguage,
            optionList = languages,
            label = "Default Vocab Language",
            onSelect = {
                defaultLanguage.value = it
                saveSettingsScope.launch {
                    userSettingsData.saveSettingsData(userSettings.copy(defaultLanguage = it))
                }
                   },
        )
        Divider()
        CounterButton(
            label = "Question Count",
            lowerLimit = 1,
            counter = defaultQuestionCount,
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.7f)
                .height(localWindowInfo.current.textFieldHeight),
            border = MaterialTheme.colorScheme.primary
        )
        Divider(8)
    }
}