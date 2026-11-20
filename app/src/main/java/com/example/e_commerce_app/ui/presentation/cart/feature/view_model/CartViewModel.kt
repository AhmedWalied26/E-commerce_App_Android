package com.example.e_commerce_app.ui.presentation.cart.feature.view_model

import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.ClearCartUseCase
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.GetCartUseCase
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.RemoveCartItemUseCase
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.UpdateCartQuantityUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CartIntent : UiIntent {
    data object LoadCart : CartIntent
    data class UpdateProductQuantity(val productId: String, val count: Int) : CartIntent
    data class RemoveProductFromCart(val productId: String) : CartIntent
    data object ClearCart : CartIntent
    data object OnCheckOutClicked : CartIntent
    data object OnBackClicked : CartIntent
}

sealed interface CartEffects : UiEffects {
    data class ShowToast(val message: String) : CartEffects
    data class NavigateToProductDetails(val productId: String) : CartEffects
    data object NavigateToCheckout : CartEffects
    data object NavigateBack : CartEffects
}

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : BaseViewModel<CartStates, CartIntent, CartEffects>(CartStates()) {

    init {
        processIntent(CartIntent.LoadCart)
    }

    override fun processIntent(intent: CartIntent) {
        when (intent) {
            CartIntent.LoadCart -> loadCart()
            is CartIntent.UpdateProductQuantity -> updateProductQuantity(intent.productId, intent.count)
            is CartIntent.RemoveProductFromCart -> removeProduct(intent.productId)
            CartIntent.ClearCart -> clearCart()
            CartIntent.OnCheckOutClicked -> onCheckOutClicked()
            CartIntent.OnBackClicked -> onBackClicked()
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
            updateState { copy(cartState = Resource.Loading) }
            when (val result = getCartUseCase()) {
                is ApiResult.ErrorApiResult -> {
                    updateState {
                        copy(cartState = Resource.Error(errorMessage = result.error.errorMessage))
                    }
                }
                is ApiResult.SuccessApiResult<Cart> -> {
                    updateState {
                        copy(cartState = Resource.Success(data = result.data))
                    }
                }
            }
        }
    }

    private fun updateProductQuantity(productId: String, count: Int) {
        if (count <= 0) {
            removeProduct(productId)
            return
        }
        viewModelScope.launch {
            updateState { copy(updatingProductId = productId) }
            when (val result = updateCartQuantityUseCase(productId, count)) {
                is ApiResult.ErrorApiResult -> {
                    updateState { copy(updatingProductId = null) }
                    sendEffects { CartEffects.ShowToast(result.error.errorMessage) }
                }
                is ApiResult.SuccessApiResult<Cart> -> {
                    updateState {
                        copy(
                            cartState = Resource.Success(data = result.data),
                            updatingProductId = null
                        )
                    }
                }
            }
        }
    }

    private fun removeProduct(productId: String) {
        viewModelScope.launch {
            updateState { copy(updatingProductId = productId) }
            when (val result = removeCartItemUseCase(productId)) {
                is ApiResult.ErrorApiResult -> {
                    updateState { copy(updatingProductId = null) }
                    sendEffects { CartEffects.ShowToast(result.error.errorMessage) }
                }
                is ApiResult.SuccessApiResult<Cart> -> {
                    updateState {
                        copy(
                            cartState = Resource.Success(data = result.data),
                            updatingProductId = null
                        )
                    }
                    sendEffects { CartEffects.ShowToast("Item removed from cart") }
                }
            }
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            updateState { copy(cartState = Resource.Loading) }
            when (val result = clearCartUseCase()) {
                is ApiResult.ErrorApiResult -> {
                    sendEffects { CartEffects.ShowToast(result.error.errorMessage) }
                }
                is ApiResult.SuccessApiResult<Cart> -> {
                    updateState {
                        copy(cartState = Resource.Success(data = result.data))
                    }
                    sendEffects { CartEffects.ShowToast("Cart cleared") }
                }
            }
        }
    }

    private fun onCheckOutClicked() {
        sendEffects { CartEffects.NavigateToCheckout }
    }

    private fun onBackClicked() {
        sendEffects { CartEffects.NavigateBack }
    }
}
