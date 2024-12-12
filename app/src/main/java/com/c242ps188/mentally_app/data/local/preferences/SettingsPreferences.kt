package com.c242ps188.mentally_app.data.local.preferences

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.c242ps188.mentally_app.util.isDarkModeEnabled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>, private val context: Context){

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map {
            it[THEME]?: context.isDarkModeEnabled()
        }
    }

    fun getLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LANGUAGE] ?: "en"
        }
    }

    suspend fun setTheme(isDarkMode: Boolean){
        dataStore.edit { preferences ->
            preferences[THEME] = isDarkMode
        }
    }

    suspend fun setLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = languageCode
        }
    }

    suspend fun clearAllData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        private val THEME = booleanPreferencesKey("theme")
        private val LANGUAGE = stringPreferencesKey("language")

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>, context: Context): SettingsPreferences{
            return instance?: synchronized(this){
                instance?: SettingsPreferences(dataStore, context)
            }.also { instance = it }
        }
    }
}