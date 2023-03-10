package com.example.mydiary.data.model

import com.google.gson.annotations.SerializedName

data class DiaryResponse(
    val id: Int,
    val title: String,
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("is_archived")
    val isArchived: Boolean
)

