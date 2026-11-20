package com.example.e_commerce_app.ui.presentation.auth.feature.register.view_model

import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.core.utils.AppValidation
import com.example.e_commerce_app.ui.presentation.auth.domain.entities.RegisterRequest
import com.example.e_commerce_app.ui.presentation.auth.domain.use_cases.RegisterUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RegisterIntent : UiIntent {
    data class NameChanged(val name: String) : RegisterIntent
    data class EmailChanged(val email: String) : RegisterIntent
    data class PhoneChanged(val phone: String) : RegisterIntent
    data class PasswordChanged(val password: String) : RegisterIntent
    data class RePasswordChanged(val rePassword: String) : RegisterIntent
    data object OnSignUpClicked : RegisterIntent
}

sealed interface RegisterEffects : UiEffects {
    data class ShowToast(val message: String) : RegisterEffects
    data object OnNavigateToHome : RegisterEffects
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val registerUseCase: RegisterUseCase
) : BaseViewModel<RegisterStates, RegisterIntent, RegisterEffects>(RegisterStates()) {

    override fun processIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.EmailChanged -> changeEmail(intent.email)
            is RegisterIntent.NameChanged -> changeName(intent.name)
            RegisterIntent.OnSignUpClicked -> register()
            is RegisterIntent.PasswordChanged -> changePassword(intent.password)
            is RegisterIntent.PhoneChanged -> changePhone(intent.phone)
            is RegisterIntent.RePasswordChanged -> changeRePassword(intent.rePassword)
        }
    }

    private fun changeName(name: String) {
        updateState { copy(name = name, nameError = null) }
    }

    private fun changeEmail(email: String) {
        updateState { copy(email = email, emailError = null) }
    }

    private fun changePhone(phone: String) {
        updateState { copy(phone = phone, phoneError = null) }
    }

    private fun changePassword(password: String) {
        updateState { copy(password = password, passwordError = null) }
    }

    private fun changeRePassword(rePassword: String) {
        updateState { copy(rePassword = rePassword, rePasswordError = null) }
    }

    private fun onSignUpClicked() {
        sendEffects { RegisterEffects.OnNavigateToHome }
    }

    private fun register() {
        val email = currentState.email
        val name = currentState.name
        val phone = currentState.phone
        val rePassword = currentState.rePassword
        val password = currentState.password

        val validateEmail = AppValidation.validateEmail(email)
        val validateName = AppValidation.validateName(name)
        val validatePhone = AppValidation.validatePhone(phone)
        val validatePassword = AppValidation.validatePassword(password)
        val validateRePassword = AppValidation.validateConfirmPassword(password, rePassword)

        updateState {
            copy(
                nameError = validateName,
                emailError = validateEmail,
                phoneError = validatePhone,
                passwordError = validatePassword,
                rePasswordError = validateRePassword
            )
        }

        if (validateName != null || validateEmail != null || validatePhone != null || validatePassword != null || validateRePassword != null)
            return

        executeRegister(
            name = name,
            email = email,
            phone = phone,
            password = password,
            rePassword = rePassword
        )
    }

    private fun executeRegister(
        name: String,
        email: String,
        phone: String,
        password: String,
        rePassword: String
    ) {
        viewModelScope.launch {
            updateState { copy(registerState = Resource.Loading) }

            when (val result = registerUseCase.invoke(
                RegisterRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    rePassword = rePassword
                )
            )) {
                is ApiResult.ErrorApiResult -> {
                    updateState {
                        copy(
                            registerState = Resource.Error(errorMessage = result.error.errorMessage)
                        )
                    }
                    //todo show toast
                    sendEffects { RegisterEffects.ShowToast(message = result.error.errorMessage) }
                }

                is ApiResult.SuccessApiResult<Unit> -> {
                    updateState {
                        copy(
                            registerState = Resource.Success(data = Unit)
                        )
                    }
                    //todo show toast
                    sendEffects { RegisterEffects.ShowToast(message = "Register Successfully") }
                    //todo navigate to home
                    sendEffects { RegisterEffects.OnNavigateToHome }
                }
            }
        }
    }
}