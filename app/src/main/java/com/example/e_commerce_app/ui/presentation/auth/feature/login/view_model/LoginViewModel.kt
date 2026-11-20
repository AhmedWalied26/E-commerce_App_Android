package com.example.e_commerce_app.ui.presentation.auth.feature.login.view_model

import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.presentation.auth.domain.entities.LoginRequest
import com.example.e_commerce_app.ui.presentation.auth.domain.use_cases.LoginUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginIntent : UiIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object OnLoginClicked : LoginIntent
    data object OnCreateAccountClicked : LoginIntent
}

sealed interface LoginEffects : UiEffects {
    data class ShowToast(val message: String) : LoginEffects
    data object OnNavigateToRegister : LoginEffects
    data object OnNavigateToHome : LoginEffects
}

@HiltViewModel
class LoginViewModel @Inject constructor(val loginUseCase: LoginUseCase) :
    BaseViewModel<LoginStates, LoginIntent, LoginEffects>(
        LoginStates()
    ) {

    override fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> changeEmail(intent.email)
            LoginIntent.OnCreateAccountClicked -> onSignUpClicked()
            LoginIntent.OnLoginClicked -> login()
            is LoginIntent.PasswordChanged -> changePassword(intent.password)
        }
    }

    private fun changeEmail(email: String) {
        updateState { copy(email = email) }
    }

    private fun changePassword(password: String) {
        updateState { copy(password = password) }
    }

    private fun onSignUpClicked() {
        sendEffects { LoginEffects.OnNavigateToRegister }
    }

    private fun login() {
        viewModelScope.launch {
            updateState { copy(loginState = Resource.Loading) }

            when (val result = loginUseCase.invoke(
                LoginRequest(
                    email = currentState.email,
                    password = currentState.password
                )
            )
            ) {
                is ApiResult.ErrorApiResult -> {
                    updateState { copy(loginState = Resource.Error(errorMessage = result.error.errorMessage)) }
                    //todo show toast
                    sendEffects { LoginEffects.ShowToast(message = result.error.errorMessage) }
                }

                is ApiResult.SuccessApiResult<Unit> -> {
                    updateState { copy(loginState = Resource.Success(data = Unit)) }
                    //todo show toast
                    sendEffects { LoginEffects.ShowToast(message = "LoginSuccessfully") }
                    //todo navigate to home
                    sendEffects { LoginEffects.OnNavigateToHome }
                }

            }
        }
    }
}