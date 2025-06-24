package com.languageApp.wengu.data.settings

import android.content.Context
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.languageApp.wengu.data.Vocab.Companion.TYPE_DELIMITER
import com.languageApp.wengu.data.settings.UserSettings.Companion.DEFAULT_LANGUAGES
import com.languageApp.wengu.data.settings.UserSettings.Companion.LANGUAGES_DELIMITER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(
    name = UserSettings.USER_SETTINGS_NAME
)
class UserSettingsData(private val context : Context?) {
    suspend fun saveSettingsData(settings: UserSettings) {
        if(context==null){
            throw NullPointerException("UserSettingsData failed to initialize context.")
        }
        context.settingsDataStore.edit {
            it[UserSettings.USEDARKMODE_KEY] = settings.useDarkMode
            it[UserSettings.LANGUAGES_KEY] = settings.languages
            it[UserSettings.TYPES_KEY] = settings.types
        }
    }
    fun getSettingsData() : Flow<UserSettings> = context!!.settingsDataStore.data.map {
        UserSettings(
            useDarkMode = it[UserSettings.USEDARKMODE_KEY] ?: -1,
            languages = it[UserSettings.LANGUAGES_KEY] ?: DEFAULT_LANGUAGES.joinToString(separator = LANGUAGES_DELIMITER),
            types = it[UserSettings.TYPES_KEY] ?: DEFAULT_LANGUAGES.joinToString(separator = TYPE_DELIMITER.toString())
        )
    }
    companion object {
        val localSettingsData : ProvidableCompositionLocal<UserSettingsData> = compositionLocalOf { UserSettingsData(null) }
    }
}