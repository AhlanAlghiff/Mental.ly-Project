package com.c242ps188.mentally_app.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UsersPreferences private constructor(private val userDataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String> {
        return userDataStore.data.map {
            it[TOKEN]?: ""
        }
    }

    fun getName(): Flow<String> {
        return userDataStore.data.map {
            it[NAME]?: ""
        }
    }

    fun getId(): Flow<String> {
        return userDataStore.data.map {
            it[ID]?: ""
        }
    }

    fun getEmail(): Flow<String> {
        return userDataStore.data.map {
            it[EMAIL]?: ""
        }
    }

    suspend fun saveLoginSession(token: String, name: String, id: String, email: String) {
        userDataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[NAME] = name
            preferences[ID] = id
            preferences[EMAIL] = email
        }

    }

    suspend fun removeLoginSession() {
        userDataStore.edit { preferences ->
            preferences.remove(TOKEN)
            preferences.remove(NAME)
            preferences.remove(ID)
            preferences.remove(EMAIL)
        }
    }

    companion object{
        private val TOKEN = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
        private val ID = stringPreferencesKey("id")
        private val EMAIL = stringPreferencesKey("email")

        @Volatile
        private var instance: UsersPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UsersPreferences{
            return instance?: synchronized(this){
                instance?: UsersPreferences(dataStore)
            }.also { instance = it }
        }
    }

}