package com.example.e_commerce_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.navigation.LoginRoute
import com.example.e_commerce_app.ui.core.navigation.MainRoute
import com.example.e_commerce_app.ui.presentation.auth.domain.use_cases.CheckIsFirstTimeUseCase
import com.example.e_commerce_app.ui.presentation.auth.domain.use_cases.GetTokenUseCase
import com.example.e_commerce_app.ui.presentation.auth.domain.use_cases.SetFirstTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getTokenUseCase: GetTokenUseCase,
    val checkIsFirstTimeUseCase: CheckIsFirstTimeUseCase,
    val setFirstTimeUseCase: SetFirstTimeUseCase
) : ViewModel() {

    private val _startDestination: MutableStateFlow<Any?> = MutableStateFlow(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkAppStartFlow()
    }

    fun checkAppStartFlow() {
        viewModelScope.launch {
            try {
                val isFirstTime = checkIsFirstTimeUseCase.invoke().first()
                val token = getTokenUseCase.invoke().first()

                _startDestination.value = when {
                    isFirstTime -> LoginRoute
                    !token.isNullOrBlank() -> MainRoute
                    else -> LoginRoute
                }
            } catch (e: Exception) {
                _startDestination.value = LoginRoute
            }
        }
    }

    fun onSplashFinished() {
        viewModelScope.launch {
            setFirstTimeUseCase.invoke()
        }
    }
}