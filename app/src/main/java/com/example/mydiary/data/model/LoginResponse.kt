package com.example.mydiary.data.model

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val code: Int,
    val error: String?
)
