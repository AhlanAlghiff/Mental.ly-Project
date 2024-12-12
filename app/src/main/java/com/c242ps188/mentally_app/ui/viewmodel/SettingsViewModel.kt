package com.c242ps188.mentally_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.c242ps188.mentally_app.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository): ViewModel() {

    val getTheme get() = settingsRepository.getTheme().asLiveData()
    val getLanguage get() = settingsRepository.getLanguage().asLiveData()

    fun setTheme(isDarkMode: Boolean){
        viewModelScope.launch {
            settingsRepository.setTheme(isDarkMode)
        }
    }

    fun setLanguage(language: String){
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
    }

    fun clearAllData(){
        viewModelScope.launch {
            settingsRepository.clearAllData()
        }
    }

}