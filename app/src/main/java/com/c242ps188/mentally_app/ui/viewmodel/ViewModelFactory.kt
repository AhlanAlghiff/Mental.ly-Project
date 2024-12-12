package com.c242ps188.mentally_app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c242ps188.mentally_app.data.repository.DiagnoseRepository
import com.c242ps188.mentally_app.data.repository.LoginRepository
import com.c242ps188.mentally_app.data.repository.SettingsRepository
import com.c242ps188.mentally_app.data.repository.UsersPreferencesRepository
import com.c242ps188.mentally_app.di.Injection

class ViewModelFactory private constructor(
    private val diagnoseRepository: DiagnoseRepository,
    private val settingsRepository: SettingsRepository,
    private val loginRepository: LoginRepository,
    private val usersPreferencesRepository: UsersPreferencesRepository,
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DiagnoseViewModel::class.java)) {
            return DiagnoseViewModel(diagnoseRepository) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsRepository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginRepository) as T
        }
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            return UsersViewModel(usersPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object{

        @Volatile
        var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance?: synchronized(this){
                instance?: ViewModelFactory(
                    Injection.provideDiagnoseRepository(),
                    Injection.provideSettingsRepository(context),
                    Injection.provideLoginRepository(),
                    Injection.provideUsersPreferencesRepository(context)
                )
            }.also { instance = it }
        }
    }
}