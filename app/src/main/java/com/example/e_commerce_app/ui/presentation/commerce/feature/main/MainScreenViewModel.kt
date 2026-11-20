package com.example.e_commerce_app.ui.presentation.commerce.feature.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.auth.domain.use_cases.ClearTokenUseCase
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.GetCartCountUseCase
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainScreenStates(
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0
) : UiStates

sealed interface MainScreenIntent : UiIntent {
    data object OnLogOutClicked : MainScreenIntent
    data class SelectTab(val index: Int) : MainScreenIntent
}

sealed interface MainScreenEffects : UiEffects {
    data object OnNavigateLogin : MainScreenEffects
}

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val clearTokenUseCase: ClearTokenUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getCartCountUseCase: GetCartCountUseCase
) : BaseViewModel<MainScreenStates, MainScreenIntent, MainScreenEffects>(
    MainScreenStates(selectedTabIndex = savedStateHandle.get<Int>(KEY_SELECTED_TAB) ?: 0)
) {

    val cartCount: StateFlow<Int> = getCartCountUseCase()

    init {
        viewModelScope.launch {
            getCartUseCase()
        }
    }

    override fun processIntent(intent: MainScreenIntent) {
        when (intent) {
            MainScreenIntent.OnLogOutClicked -> logout()
            is MainScreenIntent.SelectTab -> selectTab(intent.index)
        }
    }

    private fun selectTab(index: Int) {
        savedStateHandle[KEY_SELECTED_TAB] = index
        updateState { copy(selectedTabIndex = index) }
    }

    private fun logout() {
        viewModelScope.launch {
            //todo: remove token
            clearTokenUseCase.removeToken()
            //todo: navigate to login
            sendEffects { MainScreenEffects.OnNavigateLogin }
        }
    }

    companion object {
        private const val KEY_SELECTED_TAB = "selected_tab_index"
    }
}
