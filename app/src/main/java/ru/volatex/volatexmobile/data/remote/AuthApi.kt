package ru.volatex.volatexmobile.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import ru.volatex.volatexmobile.domain.model.Strategy
import ru.volatex.volatexmobile.domain.model.AddStrategyRequest
import ru.volatex.volatexmobile.domain.model.AddTokenRequest
import ru.volatex.volatexmobile.domain.model.Position
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

    @GET("v1/strategy/get_strategies")
    suspend fun getStrategies(
        @Header("Authorization") token: String
    ): Response<List<Strategy>>

    @POST("v1/strategy/add")
    suspend fun addStrategy(
        @Header("Authorization") token: String,
        @Body strategy: AddStrategyRequest
    ): Response<Void>

    @POST("v1/strategy/add_token")
    suspend fun addTinkoffToken(
        @Header("Authorization") authHeader: String,
        @Body tokenRequest: AddTokenRequest
    ): Response<Unit>

    @GET("v1/market/positions")
    suspend fun getPortfolio(@Header("Authorization") authHeader: String): Response<List<Position>>

}