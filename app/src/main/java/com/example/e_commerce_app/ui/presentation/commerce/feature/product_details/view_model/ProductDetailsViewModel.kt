package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.AddToCartUseCase
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.GetCartCountUseCase
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.UpdateCartQuantityUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.use_cases.GetProductDetailsUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.AddToWishlistUseCase
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.GetWishlistUseCase
import com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val getCartCountUseCase: GetCartCountUseCase
) : BaseViewModel<ProductDetailsStates, ProductDetailsIntent, ProductDetailsEffects>(ProductDetailsStates()) {

    init {
        observeCartCount()
        val productId = savedStateHandle.get<String>("productId")
        if (!productId.isNullOrBlank()) {
            processIntent(ProductDetailsIntent.LoadProductDetails(productId))
        }
    }

    private fun observeCartCount() {
        viewModelScope.launch {
            getCartCountUseCase().collect { count ->
                updateState { copy(cartCount = count) }
            }
        }
    }

    override fun processIntent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.LoadProductDetails -> loadProductDetails(intent.productId)
            is ProductDetailsIntent.IncrementQuantity -> incrementQuantity()
            is ProductDetailsIntent.DecrementQuantity -> decrementQuantity()
            is ProductDetailsIntent.SelectSize -> updateState { copy(selectedSize = intent.size) }
            is ProductDetailsIntent.SelectColor -> updateState { copy(selectedColorIndex = intent.colorIndex) }
            is ProductDetailsIntent.ToggleDescriptionExpanded -> updateState { copy(isDescriptionExpanded = !isDescriptionExpanded) }
            is ProductDetailsIntent.ToggleWishlist -> toggleWishlist()
            is ProductDetailsIntent.AddToCart -> addToCart()
        }
    }

    private fun loadProductDetails(productId: String) {
        val product = getProductDetailsUseCase(productId)
        if (product != null) {
            updateState {
                copy(
                    productDetailsState = Resource.Success(product),
                    selectedQuantity = 1
                )
            }
        } else {
            updateState {
                copy(
                    productDetailsState = Resource.Error("Product details are unavailable in the loaded catalog.")
                )
            }
        }

        viewModelScope.launch {
            when (val wishlistRes = getWishlistUseCase()) {
                is ApiResult.SuccessApiResult -> {
                    val isFav = wishlistRes.data.any { it.id == productId }
                    updateState { copy(isFavorite = isFav) }
                }
                else -> Unit
            }
        }
    }

    private fun incrementQuantity() {
        val product = (currentState.productDetailsState as? Resource.Success)?.data
        val maxStock = product?.quantity ?: Int.MAX_VALUE
        if (currentState.selectedQuantity < maxStock) {
            updateState { copy(selectedQuantity = selectedQuantity + 1) }
        } else {
            sendEffects { ProductDetailsEffects.ShowToast("Cannot exceed available stock ($maxStock)") }
        }
    }

    private fun decrementQuantity() {
        if (currentState.selectedQuantity > 1) {
            updateState { copy(selectedQuantity = selectedQuantity - 1) }
        }
    }

    private fun toggleWishlist() {
        val product = (currentState.productDetailsState as? Resource.Success)?.data ?: return
        val productId = product.id ?: return
        if (currentState.isFavoriteLoading) return

        val currentlyFavorite = currentState.isFavorite
        viewModelScope.launch {
            updateState { copy(isFavoriteLoading = true) }
            if (currentlyFavorite) {
                when (val res = removeFromWishlistUseCase(productId)) {
                    is ApiResult.SuccessApiResult -> {
                        updateState { copy(isFavorite = false, isFavoriteLoading = false) }
                        sendEffects { ProductDetailsEffects.ShowToast("Item removed from wishlist") }
                    }
                    is ApiResult.ErrorApiResult -> {
                        updateState { copy(isFavoriteLoading = false) }
                        sendEffects { ProductDetailsEffects.ShowToast(res.error.errorMessage) }
                    }
                }
            } else {
                when (val res = addToWishlistUseCase(productId)) {
                    is ApiResult.SuccessApiResult -> {
                        updateState { copy(isFavorite = true, isFavoriteLoading = false) }
                        sendEffects { ProductDetailsEffects.ShowToast("Item added to wishlist") }
                    }
                    is ApiResult.ErrorApiResult -> {
                        updateState { copy(isFavoriteLoading = false) }
                        sendEffects { ProductDetailsEffects.ShowToast(res.error.errorMessage) }
                    }
                }
            }
        }
    }

    private fun addToCart() {
        val product = (currentState.productDetailsState as? Resource.Success)?.data ?: return
        val productId = product.id ?: return
        if (currentState.isAddingToCart) return

        val qty = currentState.selectedQuantity
        val maxStock = product.quantity ?: Int.MAX_VALUE
        if (maxStock <= 0) {
            sendEffects { ProductDetailsEffects.ShowToast("Item is out of stock") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isAddingToCart = true) }
            when (val addResult = addToCartUseCase(productId)) {
                is ApiResult.ErrorApiResult -> {
                    updateState { copy(isAddingToCart = false) }
                    sendEffects { ProductDetailsEffects.ShowToast(addResult.error.errorMessage) }
                }
                is ApiResult.SuccessApiResult -> {
                    if (qty > 1) {
                        val updateResult = updateCartQuantityUseCase(productId, qty)
                        updateState { copy(isAddingToCart = false) }
                        when (updateResult) {
                            is ApiResult.ErrorApiResult -> {
                                sendEffects { ProductDetailsEffects.ShowToast("Added to cart, but couldn't update quantity: ${updateResult.error.errorMessage}") }
                            }
                            is ApiResult.SuccessApiResult -> {
                                sendEffects { ProductDetailsEffects.ShowToast("Product added to cart successfully") }
                            }
                        }
                    } else {
                        updateState { copy(isAddingToCart = false) }
                        sendEffects { ProductDetailsEffects.ShowToast("Product added to cart successfully") }
                    }
                }
            }
        }
    }
}

