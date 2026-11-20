package com.example.e_commerce_app.ui.presentation.auth.domain.use_cases

import com.example.e_commerce_app.ui.presentation.auth.data.data_source.local.AuthLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(val authLocalDataSource: AuthLocalDataSource) {
     fun invoke(): Flow<String?> = authLocalDataSource.tokenFlow
}




