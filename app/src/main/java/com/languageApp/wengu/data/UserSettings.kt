package com.languageApp.wengu.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserSettings(
    val useDarkMode : Int = -1, //-1 == null, 0 == false, 1 == true
) {

    companion object {
        const val USER_SETTINGS_NAME = "UserSettings"
        val USEDARKMODE_KEY = intPreferencesKey("UseDarkMode")
    }
}