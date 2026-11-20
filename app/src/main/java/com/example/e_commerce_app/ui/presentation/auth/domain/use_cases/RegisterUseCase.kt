package com.example.e_commerce_app.ui.presentation.auth.domain.use_cases

import com.example.e_commerce_app.ui.presentation.auth.domain.entities.RegisterRequest
import com.example.e_commerce_app.ui.presentation.auth.domain.repository.AuthRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class RegisterUseCase @Inject constructor(val authRepository: AuthRepository) {
    suspend fun invoke(registerRequest: RegisterRequest): ApiResult<Unit> =
        authRepository.register(registerRequest)
}