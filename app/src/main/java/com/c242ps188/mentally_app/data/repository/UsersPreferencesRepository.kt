package com.c242ps188.mentally_app.data.repository

import com.c242ps188.mentally_app.data.local.preferences.UsersPreferences
import kotlinx.coroutines.flow.Flow

class UsersPreferencesRepository(
    private val userPreferencesRepository: UsersPreferences
) {

    fun getToken(): Flow<String> {
        return userPreferencesRepository.getToken()
    }

    fun getName(): Flow<String> {
        return userPreferencesRepository.getName()
    }

    fun getId(): Flow<String> {
        return userPreferencesRepository.getId()
    }

    fun getEmail(): Flow<String> {
        return userPreferencesRepository.getEmail()
    }

    suspend fun saveSession(token: String, name: String, id: String, email: String) {
        userPreferencesRepository.saveLoginSession(token, name, id, email)
    }

    suspend fun removeSession() {
        userPreferencesRepository.removeLoginSession()
    }

    companion object {
        @Volatile
        private var instance: UsersPreferencesRepository? = null

        fun getInstance( userPreferencesRepository: UsersPreferences): UsersPreferencesRepository {
            return instance ?: synchronized(this) {
                instance ?: UsersPreferencesRepository(userPreferencesRepository)
            }.also { instance = it }
        }
    }

}