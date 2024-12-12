package com.c242ps188.mentally_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.c242ps188.mentally_app.data.local.model.MentalHealthRequest
import com.c242ps188.mentally_app.data.remote.ApiService
import com.c242ps188.mentally_app.data.remote.response.DiagnoseFailResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DiagnoseRepository(
    private val apiService: ApiService
) {

    private var _diagnoseConfidence = MutableLiveData<String?>()
    val diagnoseConfidence: LiveData<String?> get() = _diagnoseConfidence

    private var _diagnoseStatus = MutableLiveData<Int?>()
    val diagnoseStatus: LiveData<Int?> get() = _diagnoseStatus

    private var _diagnoseMessage = MutableLiveData<String?>()
    val diagnoseMessage: LiveData<String?> get() = _diagnoseMessage

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun submitMentalHealthData(data: MentalHealthRequest) {
        _isLoading.value = true
        try {
            val result = apiService.submitMentalHealthData(data)
            _diagnoseConfidence.value = result.confidence
            _diagnoseStatus.value = result.status
            _diagnoseMessage.value = result.message
            _isLoading.value = false
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, DiagnoseFailResponse::class.java)
            _diagnoseMessage.value = result.message
            _diagnoseStatus.value = result.status
            _isLoading.value = false
        } catch (e: SocketTimeoutException) {
            _diagnoseMessage.value = "Request timed out. Please try again."
            _isLoading.value = false
        } catch (e: UnknownHostException) {
            _diagnoseMessage.value = "No internet connection. Please check your network and try again."
            _isLoading.value = false
        } catch (e: Exception) {
            _diagnoseMessage.value = "An unexpected error occurred: ${e.message}"
            _isLoading.value = false
        }
    }

    fun resetDiagnose(){
        _diagnoseStatus.value = null
    }

    fun resetDiagnoseMessage(){
        _diagnoseMessage.value = null
        _diagnoseConfidence.value = null
    }

    companion object {
        @Volatile
        private var instance: DiagnoseRepository? = null

        fun getInstance(apiService: ApiService): DiagnoseRepository {
            return instance ?: synchronized(this) {
                instance ?: DiagnoseRepository(apiService)
            }.also { instance = it }
        }
    }
}