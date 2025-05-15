package ru.volatex.volatexmobile.data.repository

import ru.volatex.volatexmobile.data.remote.AuthApi
import ru.volatex.volatexmobile.domain.model.RegisterRequest
import ru.volatex.volatexmobile.domain.model.RegisterResponse
import ru.volatex.volatexmobile.domain.repository.AuthRepository

class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {
    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = api.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}