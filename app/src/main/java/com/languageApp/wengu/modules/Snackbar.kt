package com.languageApp.wengu.modules

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableSharedFlow

class SnackbarEvent(
    val text : String,
    val time : SnackbarDuration = SnackbarDuration.Short,
    val color : Color? = null,
    val textColor : Color? = null,
) {
    var enabled = true
    companion object {
        private val _snackbarSharedFlow : MutableSharedFlow<SnackbarEvent> = MutableSharedFlow()
        val SnackbarSharedFlow = _snackbarSharedFlow
        const val SNACKBAR_FADETIME  = 400L
        suspend fun sendSnackbarEvent(snackbarEvent: SnackbarEvent){
            _snackbarSharedFlow.emit(snackbarEvent)
        }
        suspend fun sendErrorSnackbarEvent(text: String){
            _snackbarSharedFlow.emit(
                SnackbarEvent(
                    text = text,
                    color = Color(255, 150, 150),
                    textColor = Color.White,
                    time = SnackbarDuration.Long
                )
            )
        }
    }
}
