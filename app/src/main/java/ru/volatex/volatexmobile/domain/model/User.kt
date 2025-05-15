package ru.volatex.volatexmobile.domain.model

data class User(
    val id: Int,
    val email: String
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val id: String,
    val email: String
)