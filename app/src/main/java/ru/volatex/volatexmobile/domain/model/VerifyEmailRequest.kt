package ru.volatex.volatexmobile.domain.model

data class VerifyEmailRequest(
    val code: String,
    val email: String
)
