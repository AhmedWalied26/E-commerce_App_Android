package com.example.e_commerce_app.ui.presentation.auth.data.data_source.remote

import com.example.e_commerce_app.ui.presentation.network.api.ApiServices
import com.example.e_commerce_app.ui.presentation.network.models.request.auth.login.LoginRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.request.auth.register.RegisterRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.response.auth.AuthResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.network.utilty.safeApiCall
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(val apiServices: ApiServices) :
    AuthRemoteDataSource {
    override suspend fun login(loginRequestDto: LoginRequestDto): ApiResult<AuthResponseDto> =
        safeApiCall {
            apiServices.login(loginRequestDto)
        }

    override suspend fun register(registerRequestDto: RegisterRequestDto): ApiResult<AuthResponseDto> =
        safeApiCall {
            apiServices.register(registerRequestDto)
        }
}