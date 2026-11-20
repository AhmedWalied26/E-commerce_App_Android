package com.example.e_commerce_app.ui.presentation.wishList.feature.view_model

import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.wishList.domain.entities.WishlistItem

data class WishListStates(
    val wishlistState: Resource<List<WishlistItem>> = Resource.Initial,
    val removingProductId: String? = null,
    val addingToCartProductId: String? = null
) : UiStates

sealed interface WishListIntent : UiIntent {
    data object LoadWishlist : WishListIntent
    data class RemoveFromWishlist(val productId: String) : WishListIntent
    data class AddToCart(val productId: String) : WishListIntent
}

sealed interface WishListEffects : UiEffects {
    data class ShowToast(val message: String) : WishListEffects
    data class NavigateToProductDetails(val productId: String) : WishListEffects
}
