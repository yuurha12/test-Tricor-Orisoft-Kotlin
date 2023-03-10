package com.example.mydiary.data.model

data class Diary(
    var id: Long,
    val title: String,
    val content: String,
    val isArchived: Boolean
)