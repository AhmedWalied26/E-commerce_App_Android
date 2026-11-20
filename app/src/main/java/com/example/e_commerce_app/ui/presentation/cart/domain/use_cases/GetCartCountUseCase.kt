package com.example.e_commerce_app.ui.presentation.cart.domain.use_cases

import com.example.e_commerce_app.ui.presentation.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCartCountUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): StateFlow<Int> = cartRepository.cartCount
}

