package com.c242ps188.mentally_app.di

import android.content.Context
import com.c242ps188.mentally_app.data.local.preferences.SettingsPreferences
import com.c242ps188.mentally_app.data.local.preferences.UsersPreferences
import com.c242ps188.mentally_app.data.local.preferences.dataStore
import com.c242ps188.mentally_app.data.remote.ApiConfig
import com.c242ps188.mentally_app.data.repository.DiagnoseRepository
import com.c242ps188.mentally_app.data.repository.LoginRepository
import com.c242ps188.mentally_app.data.repository.SettingsRepository
import com.c242ps188.mentally_app.data.repository.UsersPreferencesRepository

object Injection {

    fun provideSettingsRepository(context: Context): SettingsRepository {
        val pref = SettingsPreferences.getInstance(context.applicationContext.dataStore, context.applicationContext)
        return SettingsRepository.getInstance(pref)
    }

    fun provideUsersPreferencesRepository(context: Context): UsersPreferencesRepository {
        val pref = UsersPreferences.getInstance(context.applicationContext.dataStore)
        return UsersPreferencesRepository.getInstance(pref)
    }

    fun provideLoginRepository(): LoginRepository {
        val apiService = ApiConfig.getApiService()
        return LoginRepository.getInstance(apiService)
    }

    fun provideDiagnoseRepository(): DiagnoseRepository {
        val apiService = ApiConfig.getApiService()
        return DiagnoseRepository.getInstance(apiService)
    }

}