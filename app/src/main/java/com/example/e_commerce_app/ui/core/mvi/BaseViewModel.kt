package com.example.e_commerce_app.ui.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<States : UiStates, Intent : UiIntent, Effects : UiEffects>(initialValue: States) :
    ViewModel() {
    private val _states: MutableStateFlow<States> = MutableStateFlow(initialValue)
    val states: StateFlow<States> = _states.asStateFlow()

    private val _effects: Channel<Effects> = Channel(Channel.BUFFERED)
    val effects: Flow<Effects> = _effects.receiveAsFlow()

    val currentState: States
        get() = _states.value

    protected fun updateState(reducer: States.() -> States) {
        _states.update { currentState.reducer() }
    }

    protected fun sendEffects(builder: () -> Effects) {
        viewModelScope.launch { _effects.send(builder()) }
    }

    abstract fun processIntent(intent: Intent)

    fun onIntent(intent: Intent){
        processIntent(intent)
    }
}