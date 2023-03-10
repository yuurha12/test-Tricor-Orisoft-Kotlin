package com.example.mydiary.data.model

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    val passwordConfirmation: String
)