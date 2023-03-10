package com.example.mydiary.ui.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.remote.DiaryApi
import com.example.mydiary.data.model.*
import kotlinx.coroutines.launch

class DiaryViewModel(private val diaryApi: DiaryApi) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse>
        get() = _registerResponse

    private val _diaryResponse = MutableLiveData<DiaryResponse>()
    val diaryResponse: LiveData<DiaryResponse>
        get() = _diaryResponse

    private val _errorResponse = MutableLiveData<ErrorResponse>()
    val errorResponse: LiveData<ErrorResponse>
        get() = _errorResponse

    private val _diaryList = MutableLiveData<List<Diary>?>()
    val diaryList: MutableLiveData<List<Diary>?>
        get() = _diaryList

    private val _searchDiaries = MutableLiveData<LoadingStatus>()
    val searchDiaries: LiveData<LoadingStatus>
        get() = _searchDiaries

    private var _searchQuery: String = ""
    val searchQuery: String
        get() = _searchQuery



    enum class LoadingStatus {
        LOADING,
        SUCCESS,
        ERROR
    }

    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = diaryApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                _loginResponse.postValue(response.body())
                _loadingStatus.postValue(LoadingStatus.SUCCESS)
            } else {
                _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }


    fun register(username: String, email: String, password: String, passwordConfirmation: String) {
        viewModelScope.launch {
            val response = diaryApi.register(RegisterRequest(username, email, password, passwordConfirmation))
            if (response.isSuccessful) {
                _registerResponse.postValue(response.body())
                _loadingStatus.postValue(LoadingStatus.SUCCESS)
            } else {
                _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun createDiary(diary: Diary) {
        viewModelScope.launch {
            _loadingStatus.postValue(LoadingStatus.LOADING)
            val response = diaryApi.createDiary(diary)
            if (response.isSuccessful) {
                _diaryResponse.postValue(response.body())
                _loadingStatus.postValue(LoadingStatus.SUCCESS)
            } else {
                _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }


    fun getDiaries() {
        viewModelScope.launch {
            _loadingStatus.postValue(LoadingStatus.LOADING)
            try {
                val response = diaryApi.getDiaries()
                if (response.isSuccessful) {
                    val diaries = response.body()
                    if (diaries.isNullOrEmpty()) {
                        _errorResponse.postValue(ErrorResponse(-1, "No diaries found"))
                    } else {
                        _diaryResponse.postValue(diaries[0])
                        _loadingStatus.postValue(LoadingStatus.SUCCESS)
                    }
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun searchDiaries(query: LoadingStatus) {
        _loadingStatus.postValue(LoadingStatus.LOADING)
        _searchDiaries.postValue(LoadingStatus.LOADING)
        viewModelScope.launch {
            try {
                val response = diaryApi.searchDiaries(query)
                if (response.isSuccessful) {
                    val diaries = response.body()
                    if (diaries.isNullOrEmpty()) {
                        _errorResponse.postValue(ErrorResponse(-1, "No diaries found"))
                    } else {
                        _diaryList.postValue(diaries)
                        _loadingStatus.postValue(LoadingStatus.SUCCESS)
                    }
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
            _searchDiaries.postValue(LoadingStatus.SUCCESS)
        }
    }


    fun getDiaryById(diaryId: Long) {
        viewModelScope.launch {
            try {
                val response = diaryApi.getDiaryById(diaryId)
                if (response.isSuccessful) {
                    _diaryResponse.postValue(response.body())
                    _loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }


    fun updateDiary(diaryId: Long, diary: Diary) {
        viewModelScope.launch {
            try {
                val response = diaryApi.updateDiary(diaryId, diary)
                if (response.isSuccessful) {
                    _diaryResponse.postValue(response.body())
                    _loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun searchQuery(query: String) {
        _searchQuery = query
    }

    fun diaryList(): MutableLiveData<List<Diary>?> {
        val diaryList: MutableLiveData<List<Diary>?> = MutableLiveData<List<Diary>?>()
        viewModelScope.launch {
            _loadingStatus.postValue(LoadingStatus.LOADING)
            try {
                val response = diaryApi.diaryList()
                if (response.isSuccessful) {
                    val diaries = response.body()
                    diaryList.postValue(diaries)
                    _loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
        return diaryList
    }

    fun archiveDiary(diaryId: Long) {
        viewModelScope.launch {
            try {
                val response = diaryApi.archiveDiary(diaryId)
                if (response.isSuccessful) {
                    _diaryResponse.postValue(response.body())
                    _loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun unarchiveDiary(diaryId: Long) {
        _loadingStatus.postValue(LoadingStatus.LOADING)
        viewModelScope.launch {
            try {
                val response = diaryApi.unarchiveDiary(diaryId)
                if (response.isSuccessful) {
                    _diaryResponse.postValue(response.body())
                    _loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun deleteDiary(diaryId: Long) {
        viewModelScope.launch {
            _loadingStatus.postValue(LoadingStatus.LOADING)
            try {
                val response = diaryApi.deleteDiary(diaryId)
                if (response.isSuccessful) {
                    _loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else {
                    _errorResponse.postValue(ErrorResponse(response.code(), response.message()))
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _errorResponse.postValue(ErrorResponse(-1, e.message ?: "Unknown error"))
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

}
