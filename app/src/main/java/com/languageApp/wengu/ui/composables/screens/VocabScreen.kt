package com.languageApp.wengu.ui.composables.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.languageApp.wengu.data.DataEntry
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.data.Vocab
import com.languageApp.wengu.ui.localWindowInfo
import java.time.Clock
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import kotlin.time.Instant

fun Date.toString(alwaysDate : Boolean) : String {
    val instant = this.toInstant().atZone(ZoneId.systemDefault())
    val weekPassed = (Clock.systemUTC().millis()-this.toInstant().toEpochMilli())/1000 > (3600*24*7)
    return if(weekPassed && !alwaysDate   )
        "${instant.dayOfWeek.toString().lowercase().substring(0, 3).replaceFirstChar { it.uppercaseChar() } + "."} " +
                " ${if(instant.hour%12==0) "12" else instant.hour%12}:${if(instant.minute < 10) "0" else ""}${instant.minute} ${if(instant.hour >= 12) "PM" else "AM"}"
    else
        "${instant.month} ${instant.dayOfMonth} ${instant.year}"
}
@Composable
fun VocabScreen(
    vocabList : State<List<Vocab>>,
    getTestResults : suspend (DataEntry) -> List<TestResult>,
    modifier : Modifier = Modifier,
){
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(localWindowInfo.current.screenPadding)
        , horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        items(vocabList.value, key = {it.id}){vocab ->
            var clicked by remember { mutableStateOf(false) }
            val size by animateDpAsState(
                targetValue = if(clicked) localWindowInfo.current.buttonAnimateSize.times(0.5f) else localWindowInfo.current.buttonAnimateSize,
                animationSpec = tween(durationMillis = if(clicked) 10 else 100, easing = LinearOutSlowInEasing), label = "${vocab.vocab} vocab button text color",
                finishedListener = {clicked = false}
            )
            Box(
                modifier = Modifier
                    .padding(vertical = localWindowInfo.current.columnItemOffset)
                    .padding(size)
                    .fillMaxWidth()
                    .height(localWindowInfo.current.vocabItemHeight)
                    .clip(RoundedCornerShape(localWindowInfo.current.buttonRounding))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {

                    }
                    .padding(localWindowInfo.current.closeOffset)
            ){
                Text(
                    text = vocab.vocab,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = localWindowInfo.current.vocabTextStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .align(Alignment.TopCenter)
                )
                Text(
                    text = vocab.pronunciation,
                    textAlign = TextAlign.Left,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = localWindowInfo.current.buttonTextStyle,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.4f)
                        .align(Alignment.BottomStart)
                )
                Text(
                    text = Date(vocab.dateCreated).toString(false),
                    textAlign = TextAlign.Right,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = localWindowInfo.current.buttonTextStyle,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.4f)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        item{
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(localWindowInfo.current.vocabItemHeight)
            )
        }
    }
}