package com.languageApp.wengu.data.settings

import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserSettings(
    val useDarkMode : Int = -1, //-1 == null, 0 == false, 1 == true
    val languages : String = DEFAULT_LANGUAGES.joinToString(separator = LANGUAGES_DELIMITER),
) {

    companion object {
        const val USER_SETTINGS_NAME = "UserSettings"
        val USEDARKMODE_KEY = intPreferencesKey("UseDarkMode")
        val LANGUAGES_KEY = stringPreferencesKey("UseDarkMode")

        const val LANGUAGES_DELIMITER = "/"
        val DEFAULT_LANGUAGES = listOf(
            "Mandarin",
            "English",
            "Other language"
        )
        val localSettings = compositionLocalOf { UserSettings() }
    }
}
