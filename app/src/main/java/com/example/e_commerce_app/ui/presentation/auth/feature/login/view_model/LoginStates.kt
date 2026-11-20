package com.example.e_commerce_app.ui.presentation.auth.feature.login.view_model

import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource

data class LoginStates(
    val email: String = "ahmed_route@gmail.com",
    val password: String = "Ahmed@123",
    val loginState: Resource<Unit> = Resource.Initial
) : UiStates

