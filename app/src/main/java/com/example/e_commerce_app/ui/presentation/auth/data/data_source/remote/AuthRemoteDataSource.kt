package com.example.e_commerce_app.ui.presentation.auth.data.data_source.remote

import com.example.e_commerce_app.ui.presentation.network.models.request.auth.login.LoginRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.request.auth.register.RegisterRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.response.auth.AuthResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult

interface AuthRemoteDataSource {
    suspend fun login(loginRequestDto: LoginRequestDto): ApiResult<AuthResponseDto>
    suspend fun register(registerRequestDto: RegisterRequestDto): ApiResult<AuthResponseDto>
}