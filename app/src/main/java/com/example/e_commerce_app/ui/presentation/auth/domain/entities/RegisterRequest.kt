package com.example.e_commerce_app.ui.presentation.auth.domain.entities

data class RegisterRequest(

    val password: String? = null,

    val phone: String? = null,

    val rePassword: String? = null,

    val name: String? = null,

    val email: String? = null
)