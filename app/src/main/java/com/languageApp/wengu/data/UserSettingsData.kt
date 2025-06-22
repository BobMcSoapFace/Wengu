package com.languageApp.wengu.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.settingsDataStore by preferencesDataStore(
    name = UserSettings.USER_SETTINGS_NAME
)
class UserSettingsData(val context : Context) {
    suspend fun saveSettingsData(settings: UserSettings) {
        context.settingsDataStore.edit {
            it[UserSettings.USEDARKMODE_KEY] = settings.useDarkMode
        }
    }
    fun getSettingsData() : Flow<UserSettings> = context.settingsDataStore.data.map {
        UserSettings(
            useDarkMode = it[UserSettings.USEDARKMODE_KEY] ?: -1,
        )
    }
}