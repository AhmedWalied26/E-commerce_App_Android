package com.example.e_commerce_app.ui.presentation.auth.data.mappers

import com.example.e_commerce_app.ui.presentation.auth.domain.entities.RegisterRequest
import com.example.e_commerce_app.ui.presentation.network.models.request.auth.register.RegisterRequestDto

fun RegisterRequest.toRegisterRequestDto(): RegisterRequestDto {
    return RegisterRequestDto(
        name = name,
        phone = phone,
        email = email,
        password = password,
        rePassword = rePassword
    )
}