package com.example.e_commerce_app.ui.presentation.auth.domain.use_cases

import com.example.e_commerce_app.ui.presentation.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SetFirstTimeUseCase @Inject constructor(
    val authRepository: AuthRepository
) {
   suspend fun invoke(): Unit =
        authRepository.setFirstTimeCompleted()
}