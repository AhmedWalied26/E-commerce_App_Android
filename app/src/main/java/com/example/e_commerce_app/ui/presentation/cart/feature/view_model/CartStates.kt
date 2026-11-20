package com.example.e_commerce_app.ui.presentation.cart.feature.view_model

import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource

data class CartStates(
    val cartState: Resource<Cart> = Resource.Initial,
    val updatingProductId: String? = null
) : UiStates

