package com.c242ps188.mentally_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242ps188.mentally_app.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {

    val loginMessage get() = loginRepository.loginMessage
    val loginResult get() = loginRepository.loginResult
    val userToken get() = loginRepository.userToken
    val registerMessage get() = loginRepository.registerMessage
    val registerError get() = loginRepository.registerError
    val isLoading get() = loginRepository.isLoading

    fun register(name: String, email: String, password: String, confPassword: String){
        viewModelScope.launch {
            loginRepository.register(name, email, password, confPassword)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(email, password)
        }
    }
}