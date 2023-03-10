package com.example.mydiary.ui.diary

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.data.remote.DiaryApi


@Suppress("UNCHECKED_CAST")
class DiaryViewModelFactory(private val diaryApi: DiaryApi, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(diaryApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

