package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.view_model

import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.AddToCartUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.use_cases.GetProductsUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.AddToWishlistUseCase
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.GetWishlistUseCase
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductIntent : UiIntent {
    data object LoadProducts : ProductIntent
    data object RefreshWishlist : ProductIntent
    data class AddToCart(val productId: String) : ProductIntent
    data class ToggleWishlist(val productId: String) : ProductIntent
}

sealed interface ProductEffects : UiEffects {
    data class ShowToast(val message: String) : ProductEffects
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val getWishlistUseCase: GetWishlistUseCase
) : BaseViewModel<ProductStates, ProductIntent, ProductEffects>(ProductStates()) {

    init {
        processIntent(ProductIntent.LoadProducts)
    }

    override fun processIntent(intent: ProductIntent) {
        when (intent) {
            ProductIntent.LoadProducts -> loadProduct()
            ProductIntent.RefreshWishlist -> refreshWishlist()
            is ProductIntent.AddToCart -> addToCart(intent.productId)
            is ProductIntent.ToggleWishlist -> toggleWishlist(intent.productId)
        }
    }

    private fun addToCart(productId: String) {
        viewModelScope.launch {
            when (val result = addToCartUseCase(productId)) {
                is ApiResult.ErrorApiResult -> {
                    sendEffects { ProductEffects.ShowToast(result.error.errorMessage) }
                }

                is ApiResult.SuccessApiResult -> {
                    sendEffects { ProductEffects.ShowToast("Added to cart successfully") }
                }
            }
        }
    }

    private fun toggleWishlist(productId: String) {
        if (productId.isEmpty()) return
        if (currentState.wishlistLoadingProductIds.contains(productId)) return

        val isCurrentlyFavorite = currentState.favoriteProductIds.contains(productId)
        viewModelScope.launch {
            updateState {
                copy(wishlistLoadingProductIds = wishlistLoadingProductIds + productId)
            }
            if (isCurrentlyFavorite) {
                when (val result = removeFromWishlistUseCase(productId)) {
                    is ApiResult.ErrorApiResult -> {
                        updateState {
                            copy(wishlistLoadingProductIds = wishlistLoadingProductIds - productId)
                        }
                        sendEffects { ProductEffects.ShowToast(result.error.errorMessage) }
                    }

                    is ApiResult.SuccessApiResult -> {
                        updateState {
                            copy(
                                favoriteProductIds = favoriteProductIds - productId,
                                wishlistLoadingProductIds = wishlistLoadingProductIds - productId
                            )
                        }
                        sendEffects { ProductEffects.ShowToast("Product removed successfully from your wishlist") }
                    }
                }
            } else {
                when (val result = addToWishlistUseCase(productId)) {
                    is ApiResult.ErrorApiResult -> {
                        updateState {
                            copy(wishlistLoadingProductIds = wishlistLoadingProductIds - productId)
                        }
                        sendEffects { ProductEffects.ShowToast(result.error.errorMessage) }
                    }

                    is ApiResult.SuccessApiResult -> {
                        updateState {
                            copy(
                                favoriteProductIds = favoriteProductIds + productId,
                                wishlistLoadingProductIds = wishlistLoadingProductIds - productId
                            )
                        }
                        sendEffects { ProductEffects.ShowToast("Product added successfully to your wishlist") }
                    }
                }
            }
        }
    }

    private fun refreshWishlist() {
        viewModelScope.launch {
            val wishlistResult = getWishlistUseCase()
            if (wishlistResult is ApiResult.SuccessApiResult) {
                val favIds = wishlistResult.data.map { it.id }.toSet()
                updateState { copy(favoriteProductIds = favIds) }
            }
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            updateState {
                copy(productState = Resource.Loading)
            }
            when (val result = getProductsUseCase.invoke()) {
                is ApiResult.ErrorApiResult -> updateState {
                    copy(productState = Resource.Error(errorMessage = result.error.errorMessage))
                }

                is ApiResult.SuccessApiResult<List<ProductItem>> -> {
                    // Fetch existing wishlist in-memory so products display accurate favorite states
                    val wishlistResult = getWishlistUseCase()
                    val favIds = if (wishlistResult is ApiResult.SuccessApiResult) {
                        wishlistResult.data.map { it.id }.toSet()
                    } else {
                        currentState.favoriteProductIds
                    }
                    updateState {
                        copy(
                            productState = Resource.Success(data = result.data),
                            favoriteProductIds = favIds
                        )
                    }
                    sendEffects { ProductEffects.ShowToast(message = "Loaded Successfully") }
                }
            }
        }
    }
}
