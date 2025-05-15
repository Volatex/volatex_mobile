package ru.volatex.volatexmobile.domain.repository

import ru.volatex.volatexmobile.domain.model.RegisterRequest
import ru.volatex.volatexmobile.domain.model.RegisterResponse

interface AuthRepository {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
}