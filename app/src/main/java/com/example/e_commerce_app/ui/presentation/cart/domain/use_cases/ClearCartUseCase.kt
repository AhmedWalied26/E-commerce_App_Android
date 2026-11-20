package com.example.e_commerce_app.ui.presentation.cart.domain.use_cases

import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.repository.CartRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): ApiResult<Cart> = cartRepository.clearCart()
}

