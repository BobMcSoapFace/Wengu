package com.languageApp.wengu.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.languageApp.wengu.ui.AnimateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class LanguageViewModel(
    private val application : Application
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.DATE_UPDATED)
    private val _animateState = MutableStateFlow(AnimateState())

    val userSettings: UserSettingsData = UserSettingsData(application.applicationContext)
    val userSettingsState = userSettings.getSettingsData().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), UserSettings())
    val animationState =_animateState.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), AnimateState()
    )
    val sortTypeState = _sortType.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), SortType.DATE_UPDATED)
    suspend fun setAnimateState(animateState: AnimateState){
        _animateState.emit(animateState)
    }


}