package com.example.mydiary.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.model.LoginRequest
import com.example.mydiary.data.model.LoginResponse
import com.example.mydiary.data.model.ErrorResponse
import com.example.mydiary.data.remote.DiaryApi
import kotlinx.coroutines.launch

class LoginViewModel(private val apiClient: DiaryApi) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun login(loginrequest: LoginRequest, erorr: ErrorResponse, loginResponse: LoginResponse) {
        viewModelScope.launch {
            val response = apiClient.login(loginrequest)
            if (response.isSuccessful && response.body() != null) {
                _loginResponse.postValue(response.body())
            } else {
                val errorResponse = response.errorBody()?.let { erorr}
                _loginResponse.postValue(loginResponse)
            }
        }
    }
}