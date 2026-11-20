package com.example.e_commerce_app.ui.presentation.auth.data.mappers

import com.example.e_commerce_app.ui.presentation.auth.domain.entities.LoginRequest
import com.example.e_commerce_app.ui.presentation.network.models.request.auth.login.LoginRequestDto

fun LoginRequest.toLoginRequestDto(): LoginRequestDto {
    return LoginRequestDto(
        password = password,
        email = email
    )
}