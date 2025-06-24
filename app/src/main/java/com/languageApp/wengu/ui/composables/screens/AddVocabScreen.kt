package com.languageApp.wengu.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.data.DataAction
import com.languageApp.wengu.data.DataEntry
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.buttons.FieldSelection
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.composables.units.buttons.ListSelection
import com.languageApp.wengu.ui.localWindowInfo
import kotlinx.serialization.json.Json

@Composable
fun AddVocabScreen(
    vocabList : State<List<Vocab>>,
    onDataAction : (DataAction) -> Unit,
    navigateTo : (String) -> Unit,
    navigateBack : () -> Unit,
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
    val vocabName = rememberSaveable{ mutableStateOf("") }
    val pronunciation = rememberSaveable{ mutableStateOf("") }
    val translation = rememberSaveable{ mutableStateOf("") }
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
                val testList = rememberSaveable { mutableStateOf(listOf("item1", "item2", "item3")) }
                val testSelected = rememberSaveable { mutableStateOf(testList.value) }
                ListSelection(
                    list = testList,
                    selected = testSelected,
                    onClick = {
                        testSelected.value -= it
                              },
                    onSelect = {
                        testSelected.value += (it)
                    },
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    height = localWindowInfo.current.longButtonHeight,
                    width = localWindowInfo.current.longButtonWidth *
                            (if(localWindowInfo.current.screenHeightInfo == WindowInfo.WindowType.Compact) 3 else 1),
                    modifier = Modifier,
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
                function = {},
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