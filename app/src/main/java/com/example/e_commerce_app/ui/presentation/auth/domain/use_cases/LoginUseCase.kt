package com.example.e_commerce_app.ui.presentation.auth.domain.use_cases

import com.example.e_commerce_app.ui.presentation.auth.domain.entities.LoginRequest
import com.example.e_commerce_app.ui.presentation.auth.domain.repository.AuthRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(val authRepository: AuthRepository) {
    suspend fun invoke(loginRequest: LoginRequest): ApiResult<Unit> =
        authRepository.login(loginRequest)
}