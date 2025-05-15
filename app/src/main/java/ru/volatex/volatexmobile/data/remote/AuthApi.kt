package ru.volatex.volatexmobile.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.volatex.volatexmobile.domain.model.RegisterRequest
import ru.volatex.volatexmobile.domain.model.RegisterResponse
import ru.volatex.volatexmobile.domain.model.SignInRequest
import ru.volatex.volatexmobile.domain.model.SignInResponse
import ru.volatex.volatexmobile.domain.model.VerifyEmailRequest

interface AuthApi {
    @POST("v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("v1/auth/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<RegisterResponse>

    @POST("v1/auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>
}