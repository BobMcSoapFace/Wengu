package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.languageApp.wengu.data.DataAction
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.data.settings.UserSettings
import com.languageApp.wengu.data.settings.UserSettingsData
import com.languageApp.wengu.modules.SnackbarEvent
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.buttons.FieldSelection
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.composables.units.buttons.ListSelection
import com.languageApp.wengu.ui.localWindowInfo
import com.languageApp.wengu.ui.theme.DebugState
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun AddVocabScreen(
    vocabList : State<List<Vocab>>,
    onDataAction : (DataAction) -> Unit,
    navigateTo : (String) -> Unit,
    navigateBack : () -> Unit,
    editingVocab : Vocab? = null,
){
    @Composable
    fun Divider(){
        Box(modifier = Modifier
            .height(localWindowInfo.current.dividerHeight)
            .fillMaxWidth())
    }
    @Composable
    fun Footnote(text : String, modifier: Modifier = Modifier){
        Box(modifier = modifier
            .fillMaxWidth(1f)
            .padding(localWindowInfo.current.closeOffset)
            .height(IntrinsicSize.Max)
        ){
            Text(
                text = text,
                style = localWindowInfo.current.footnoteTextStyle,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Left,
                modifier = Modifier.align(
                    if(localWindowInfo.current.screenHeightInfo != WindowInfo.WindowType.Compact) Alignment.CenterStart
                    else Alignment.Center
                )
            )
        }
    }
    val userSettings = UserSettings.localSettings.current
    val coroutineScope = rememberCoroutineScope()
    val vocabName = rememberSaveable{ mutableStateOf(editingVocab?.vocab ?: "") }
    val pronunciation = rememberSaveable{ mutableStateOf(editingVocab?.pronunciation ?:"") }
    val translation = rememberSaveable{ mutableStateOf(editingVocab?.translation ?:"") }
    val typeList = rememberSaveable { mutableStateOf(userSettings.types.split(Vocab.TYPE_DELIMITER).map { it -> it.lowercase().replaceFirstChar{ it.uppercaseChar() }}) }
    val languageList = rememberSaveable { mutableStateOf(userSettings.languages.split(Vocab.LANGUAGES_DELIMITER).map { it -> it.lowercase().replaceFirstChar{ it.uppercaseChar() }}) }
    val typesSelected = rememberSaveable { mutableStateOf(
        editingVocab?.type?.split(Vocab.TYPE_DELIMITER) ?: listOf())}
    val languageSelected = rememberSaveable { mutableStateOf(
        editingVocab?.vocabLanguage?.split(Vocab.LANGUAGES_DELIMITER) ?: listOf())}
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(localWindowInfo.current.screenPadding)
            , horizontalAlignment = Alignment.CenterHorizontally
            , userScrollEnabled = true
        ) {
            item {
                FieldSelection(
                    inputValue = vocabName.value,
                    onInputChange = { vocabName.value = it },
                    label = "Name",
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    onTextColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(localWindowInfo.current.textFieldHeight)
                )
                Footnote("word(s) of vocabulary term")
                Divider()
                FieldSelection(
                    inputValue = pronunciation.value,
                    onInputChange = { pronunciation.value = it },
                    label = "Pronunciation",
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    onTextColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(localWindowInfo.current.textFieldHeight)
                )
                Footnote("the pronunciation of the term - \"${pronunciation.value}\"")
                Divider()
                FieldSelection(
                    inputValue = translation.value,
                    onInputChange = { translation.value = it },
                    label = "Definition",
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    onTextColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(localWindowInfo.current.textFieldHeight)
                )
                Footnote("the definition when translated - \"${translation.value}\"")
                Divider()
                ListSelection(
                    label = "Type",
                    list = typeList,
                    selected = typesSelected,
                    onClick = {
                        typesSelected.value -= it
                              },
                    onSelect = {
                        typesSelected.value += (it)
                    },
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    height = localWindowInfo.current.longButtonHeight,
                    width = localWindowInfo.current.longButtonWidth *
                            (if(localWindowInfo.current.screenHeightInfo == WindowInfo.WindowType.Compact) 3 else 1),
                    modifier = Modifier,
                )
                Divider()
                ListSelection(
                    label = "Language",
                    list = languageList,
                    selected = languageSelected,
                    onClick = {
                        languageSelected.value -= it
                    },
                    onSelect = {
                        languageSelected.value += (it)
                    },
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    height = localWindowInfo.current.longButtonHeight,
                    width = localWindowInfo.current.longButtonWidth *
                            (if(localWindowInfo.current.screenHeightInfo == WindowInfo.WindowType.Compact) 3 else 1),
                    modifier = Modifier,
                    limit = 1,
                )
                Divider()
                Divider()
            }
        }
        IconButton(
            iconInteractableIcon = InteractableIcon(
                function = {navigateBack()},
                label = "",
                icon = Icons.Default.ArrowDropDown,
                color = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onTextColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .width(localWindowInfo.current.landscapeButtonHeight)
                .height(localWindowInfo.current.landscapeButtonHeight)
                .align(Alignment.BottomStart)
                .offset(
                    x = localWindowInfo.current.screenPadding,
                    y = -localWindowInfo.current.screenPadding,
                ),
            expanded = true,
            showText = false,
            haveBackgroundBorder = MaterialTheme.colorScheme.primary
        )
        IconButton(
            iconInteractableIcon = InteractableIcon(
                function = {
                    if(languageSelected.value.isEmpty() || languageSelected.value.first().isEmpty()){
                        coroutineScope.launch {
                            SnackbarEvent.sendSnackbarEvent(
                                SnackbarEvent(
                                    text = "Vocab must have a language selected.",
                                    time = SnackbarDuration.Short,
                                    color = DebugState.SNACKBAR_BACKGROUND.color,
                                    textColor = DebugState.SNACKBAR_TEXT.color
                                )
                            )
                        }
                        return@InteractableIcon
                    }
                    val newVocab = Vocab(
                        id = editingVocab?.id ?: 0,
                        vocab = vocabName.value,
                        pronunciation = pronunciation.value,
                        translation = translation.value,
                        type = typesSelected.value.joinToString(Vocab.TYPE_DELIMITER),
                        vocabLanguage = languageSelected.value.joinToString(Vocab.LANGUAGES_DELIMITER),
                        dateCreated = Instant.now().epochSecond
                    )
                    onDataAction(DataAction.Upsert(newVocab))
                    navigateBack()
                    coroutineScope.launch {
                        SnackbarEvent.sendSnackbarEvent(
                            SnackbarEvent(
                                text = if(editingVocab != null) "Updated vocab!" else "Created vocab!",
                                time = SnackbarDuration.Short,
                                color = DebugState.SUCCESS.color,
                                textColor = DebugState.SNACKBAR_TEXT.color
                            )
                        )
                    }
                },
                label = "Add",
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
                    y = -localWindowInfo.current.screenPadding,
                ),
            expanded = localWindowInfo.current.screenHeightInfo != WindowInfo.WindowType.Compact,
            haveBackgroundBorder = MaterialTheme.colorScheme.primary
        )
    }
}