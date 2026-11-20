package com.example.e_commerce_app.ui.presentation.cart.domain.use_cases

import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.repository.CartRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String, count: Int): ApiResult<Cart> =
        cartRepository.updateProductQuantity(productId, count)
}

