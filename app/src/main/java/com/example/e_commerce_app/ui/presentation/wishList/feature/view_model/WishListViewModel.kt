package com.example.e_commerce_app.ui.presentation.wishList.feature.view_model

import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.AddToCartUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.GetWishlistUseCase
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : BaseViewModel<WishListStates, WishListIntent, WishListEffects>(WishListStates()) {

    init {
        processIntent(WishListIntent.LoadWishlist)
    }

    override fun processIntent(intent: WishListIntent) {
        when (intent) {
            WishListIntent.LoadWishlist -> loadWishlist()
            is WishListIntent.RemoveFromWishlist -> removeProduct(intent.productId)
            is WishListIntent.AddToCart -> addToCart(intent.productId)
        }
    }

    private fun loadWishlist() {
        viewModelScope.launch {
            updateState { copy(wishlistState = Resource.Loading) }
            when (val result = getWishlistUseCase()) {
                is ApiResult.ErrorApiResult -> {
                    updateState {
                        copy(wishlistState = Resource.Error(errorMessage = result.error.errorMessage))
                    }
                }
                is ApiResult.SuccessApiResult -> {
                    updateState {
                        copy(wishlistState = Resource.Success(data = result.data))
                    }
                }
            }
        }
    }

    private fun removeProduct(productId: String) {
        if (currentState.removingProductId == productId) return

        viewModelScope.launch {
            updateState { copy(removingProductId = productId) }
            when (val result = removeFromWishlistUseCase(productId)) {
                is ApiResult.ErrorApiResult -> {
                    updateState { copy(removingProductId = null) }
                    sendEffects { WishListEffects.ShowToast(result.error.errorMessage) }
                }
                is ApiResult.SuccessApiResult -> {
                    val currentList = (currentState.wishlistState as? Resource.Success)?.data.orEmpty()
                    val updatedList = currentList.filterNot { it.id == productId }
                    updateState {
                        copy(
                            wishlistState = Resource.Success(updatedList),
                            removingProductId = null
                        )
                    }
                    sendEffects { WishListEffects.ShowToast("Product removed successfully from your wishlist") }
                }
            }
        }
    }

    private fun addToCart(productId: String) {
        if (currentState.addingToCartProductId == productId) return

        viewModelScope.launch {
            updateState { copy(addingToCartProductId = productId) }
            when (val result = addToCartUseCase(productId)) {
                is ApiResult.ErrorApiResult -> {
                    updateState { copy(addingToCartProductId = null) }
                    sendEffects { WishListEffects.ShowToast(result.error.errorMessage) }
                }
                is ApiResult.SuccessApiResult -> {
                    updateState { copy(addingToCartProductId = null) }
                    sendEffects { WishListEffects.ShowToast("Added to cart successfully") }
                }
            }
        }
    }
}
