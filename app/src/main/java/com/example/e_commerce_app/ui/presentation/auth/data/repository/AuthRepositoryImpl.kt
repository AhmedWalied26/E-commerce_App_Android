package com.example.e_commerce_app.ui.presentation.auth.data.repository

import android.text.BoringLayout
import com.example.e_commerce_app.ui.core.errors.AppErrors
import com.example.e_commerce_app.ui.presentation.auth.data.data_source.local.AuthLocalDataSource
import com.example.e_commerce_app.ui.presentation.auth.data.data_source.remote.AuthRemoteDataSource
import com.example.e_commerce_app.ui.presentation.auth.data.mappers.toLoginRequestDto
import com.example.e_commerce_app.ui.presentation.auth.data.mappers.toRegisterRequestDto
import com.example.e_commerce_app.ui.presentation.auth.domain.entities.LoginRequest
import com.example.e_commerce_app.ui.presentation.auth.domain.entities.RegisterRequest
import com.example.e_commerce_app.ui.presentation.auth.domain.repository.AuthRepository
import com.example.e_commerce_app.ui.presentation.network.models.response.auth.AuthResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val authRemoteDataSource: AuthRemoteDataSource, val authLocalDataSource: AuthLocalDataSource
) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest): ApiResult<Unit> {
        return when (val result = authRemoteDataSource.login(loginRequest.toLoginRequestDto())) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(
                    message = result.error.errorMessage
                )
            )

            is ApiResult.SuccessApiResult<AuthResponseDto> -> {
                //todo save token
                val token = result.data.token
                if (!token.isNullOrBlank()) authLocalDataSource.saveToken(token)
                ApiResult.SuccessApiResult(data = Unit)
            }
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): ApiResult<Unit> {
        return when (val result =
            authRemoteDataSource.register(registerRequest.toRegisterRequestDto())) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(
                    message = result.error.errorMessage
                )
            )

            is ApiResult.SuccessApiResult<AuthResponseDto> -> {
                //todo save token
                val token = result.data.token
                if (!token.isNullOrBlank()) authLocalDataSource.saveToken(token)
                ApiResult.SuccessApiResult(data = Unit)
            }
        }
    }

    override val isFirstTime: Flow<Boolean>
        get() = authLocalDataSource.isFirstTime

    override suspend fun setFirstTimeCompleted() {
        authLocalDataSource.setFirstTimeCompleted()
    }

    override suspend fun removeToken() {
        authLocalDataSource.removeToken()
    }
}