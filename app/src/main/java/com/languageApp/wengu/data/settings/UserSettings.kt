package com.languageApp.wengu.data.settings

import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.languageApp.wengu.data.Vocab.Companion.TYPE_DELIMITER

data class UserSettings(
    val useDarkMode : Int = -1, //-1 == null, 0 == false, 1 == true
    val languages : String = DEFAULT_LANGUAGES.joinToString(separator = LANGUAGES_DELIMITER),
    val types : String = DEFAULT_TYPES.joinToString(separator = TYPE_DELIMITER.toString())
) {

    companion object {
        const val USER_SETTINGS_NAME = "UserSettings"
        val USEDARKMODE_KEY = intPreferencesKey("UseDarkMode")
        val LANGUAGES_KEY = stringPreferencesKey("Languages")
        val TYPES_KEY = stringPreferencesKey("Types")

        const val LANGUAGES_DELIMITER = "/"
        val DEFAULT_LANGUAGES = listOf(
            "Mandarin",
            "English",
            "Other language"
        )
        val DEFAULT_TYPES = listOf(
            "Noun",
            "Adjective",
            "Verb",
            "Adverb",
            "Phrase",
            "Preposition",
            "Quantifier"
        )
        val localSettings = compositionLocalOf { UserSettings() }
    }
}
