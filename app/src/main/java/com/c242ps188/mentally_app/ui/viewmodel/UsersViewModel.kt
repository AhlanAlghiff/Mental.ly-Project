package com.c242ps188.mentally_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.c242ps188.mentally_app.data.repository.UsersPreferencesRepository
import kotlinx.coroutines.launch

class UsersViewModel(private val usersPreferencesRepository: UsersPreferencesRepository) : ViewModel() {

    val getToken get() = usersPreferencesRepository.getToken().asLiveData()
    val getName get() = usersPreferencesRepository.getName().asLiveData()
    val getId get() = usersPreferencesRepository.getId().asLiveData()
    val getEmail get() = usersPreferencesRepository.getEmail().asLiveData()

    fun saveSession(token: String, name: String, id: String, email: String){
        viewModelScope.launch {
            usersPreferencesRepository.saveSession(token, name, id, email)
        }
    }

    fun removeSession(){
        viewModelScope.launch {
            usersPreferencesRepository.removeSession()
        }
    }

}