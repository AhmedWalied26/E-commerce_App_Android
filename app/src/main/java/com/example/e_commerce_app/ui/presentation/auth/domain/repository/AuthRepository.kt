package com.example.e_commerce_app.ui.presentation.auth.domain.repository

import com.example.e_commerce_app.ui.presentation.auth.domain.entities.LoginRequest
import com.example.e_commerce_app.ui.presentation.auth.domain.entities.RegisterRequest
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): ApiResult<Unit>
    suspend fun register(registerRequest: RegisterRequest): ApiResult<Unit>

    val isFirstTime : Flow<Boolean>

    suspend fun setFirstTimeCompleted()

    suspend fun removeToken()
}