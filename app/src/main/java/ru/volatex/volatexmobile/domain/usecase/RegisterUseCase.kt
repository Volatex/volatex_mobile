package ru.volatex.volatexmobile.domain.usecase.RegisterUseCase

import ru.volatex.volatexmobile.domain.model.RegisterRequest
import ru.volatex.volatexmobile.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: RegisterRequest) = repository.register(request)
}