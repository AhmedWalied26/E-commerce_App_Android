package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.view_model

import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource

data class ProductStates(
    val productState: Resource<List<ProductItem>> = Resource.Initial,
    val favoriteProductIds: Set<String> = emptySet(),
    val wishlistLoadingProductIds: Set<String> = emptySet()
) : UiStates
