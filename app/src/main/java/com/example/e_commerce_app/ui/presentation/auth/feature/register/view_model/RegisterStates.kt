package com.example.e_commerce_app.ui.presentation.auth.feature.register.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource

data class RegisterStates(
    val name: String = "ahmed",
    val phone: String = "00110011001",
    val email: String = "ahmed0@route.com",
    val password: String = "Ahmed@123",
    val rePassword: String = "Ahmed@123",

    var nameError: String? = null,
    var phoneError: String? = null,
    var emailError: String? = null,
    var passwordError: String? = null,
    var rePasswordError: String? = null,

    val registerState: Resource<Unit> = Resource.Initial
) : UiStates
