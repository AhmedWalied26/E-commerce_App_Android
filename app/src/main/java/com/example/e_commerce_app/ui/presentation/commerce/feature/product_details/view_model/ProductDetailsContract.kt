package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.view_model

import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource

data class ProductDetailsStates(
    val productDetailsState: Resource<ProductItem> = Resource.Initial,
    val selectedQuantity: Int = 1,
    val selectedSize: String = "40",
    val selectedColorIndex: Int = 1,
    val isFavorite: Boolean = false,
    val isFavoriteLoading: Boolean = false,
    val isAddingToCart: Boolean = false,
    val cartCount: Int = 0,
    val isDescriptionExpanded: Boolean = false
) : UiStates

sealed interface ProductDetailsIntent : UiIntent {
    data class LoadProductDetails(val productId: String) : ProductDetailsIntent
    data object IncrementQuantity : ProductDetailsIntent
    data object DecrementQuantity : ProductDetailsIntent
    data class SelectSize(val size: String) : ProductDetailsIntent
    data class SelectColor(val colorIndex: Int) : ProductDetailsIntent
    data object ToggleDescriptionExpanded : ProductDetailsIntent
    data object ToggleWishlist : ProductDetailsIntent
    data object AddToCart : ProductDetailsIntent
}

sealed interface ProductDetailsEffects : UiEffects {
    data class ShowToast(val message: String) : ProductDetailsEffects
    data object NavigateBack : ProductDetailsEffects
    data object NavigateToCart : ProductDetailsEffects
}

