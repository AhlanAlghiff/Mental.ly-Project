package com.c242ps188.mentally_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242ps188.mentally_app.data.local.model.MentalHealthRequest
import com.c242ps188.mentally_app.data.repository.DiagnoseRepository
import kotlinx.coroutines.launch

class DiagnoseViewModel(private val diagnoseRepository: DiagnoseRepository): ViewModel() {

    private val _diagnoseProgress = MutableLiveData<Int>()
    val diagnoseProgress: LiveData<Int> get() = _diagnoseProgress

    var gender: Int? = null
    var age: Float? = null
    var workPressure: Float? = null
    var workSatisfied: Float? = null
    var stressLevel: Float? = null
    var dietaryHabits: Float? = null
    var sleepHours: Float? = null
    var workHours: Float? = null
    var selfHarm: Float? = null
    var historyMental: Float? = null

    val diagnoseStatus get() = diagnoseRepository.diagnoseStatus
    val diagnoseMessage get() = diagnoseRepository.diagnoseMessage
    val diagnoseConfidence get() = diagnoseRepository.diagnoseConfidence
    val isLoading get() = diagnoseRepository.isLoading

    init {
        _diagnoseProgress.value = 1
    }

    fun submitMentalHealthData(data: MentalHealthRequest){
        viewModelScope.launch {
            diagnoseRepository.submitMentalHealthData(data)
        }
    }

    fun incrementProgress() {
        if (_diagnoseProgress.value != 11){
            val currentProgress = _diagnoseProgress.value ?: 0
            _diagnoseProgress.value = currentProgress + 1
        }
    }

    fun decrementProgress() {
        val currentProgress = _diagnoseProgress.value ?: 0
        if (currentProgress > 0) {
            _diagnoseProgress.value = currentProgress - 1
        }
    }

    fun resetProgress() {
        diagnoseRepository.resetDiagnose()
        _diagnoseProgress.value = 1
    }

    fun resetDiagnoseMessage(){
        diagnoseRepository.resetDiagnoseMessage()
    }
}