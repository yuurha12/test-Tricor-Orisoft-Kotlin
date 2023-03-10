package com.example.mydiary.data.model

data class RegisterResponse(
    val success: Boolean,
    val user: RegisterRequest,
    val token: String,
    val code: Int,
    val error: String?
)