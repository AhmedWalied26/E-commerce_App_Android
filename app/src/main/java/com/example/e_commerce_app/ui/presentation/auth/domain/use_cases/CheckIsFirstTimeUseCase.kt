package com.example.e_commerce_app.ui.presentation.auth.domain.use_cases

import com.example.e_commerce_app.ui.presentation.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckIsFirstTimeUseCase @Inject constructor(
    val authRepository: AuthRepository
) {
    fun invoke(): Flow<Boolean> = authRepository.isFirstTime
}