package com.languageApp.wengu.ui.composables.units

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.languageApp.wengu.ui.WindowInfo
import com.languageApp.wengu.ui.localWindowInfo

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