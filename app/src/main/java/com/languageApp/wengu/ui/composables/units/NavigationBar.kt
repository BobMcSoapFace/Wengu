package com.languageApp.wengu.ui.composables.units

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.languageApp.wengu.ui.InteractableIcon
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.composables.units.buttons.IconButton
import com.languageApp.wengu.ui.localWindowInfo

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavigationBar(
    modifier : Modifier,
    buttons : List<InteractableIcon>,
    buttonColor : Color = MaterialTheme.colorScheme.primary,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    onButtonColor : Color = textColor,
    onTextColor : Color = buttonColor,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .then(modifier)
        , contentAlignment = Alignment.Center
    ){
        FlowRow(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(if(localWindowInfo.current.screenHeightInfo != WindowInfo.WindowType.Compact) 0.875f else 0.5f)
                .padding(vertical = localWindowInfo.current.closeOffset),
            maxLines = 1,
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            buttons.forEach{interactableIcon ->
                IconButton(
                    iconInteractableIcon = interactableIcon,
                    buttonColor = buttonColor,
                    textColor = textColor,
                    onButtonColor = onButtonColor,
                    onTextColor = onTextColor,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f, true)
                )
            }
        }
    }
}