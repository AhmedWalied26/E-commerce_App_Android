package com.example.e_commerce_app.ui.presentation.cart.domain.repository

import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartCount: StateFlow<Int>
    suspend fun getCart(): ApiResult<Cart>
    suspend fun addToCart(productId: String): ApiResult<Unit>
    suspend fun updateProductQuantity(productId: String, count: Int): ApiResult<Cart>
    suspend fun removeCartItem(productId: String): ApiResult<Cart>
    suspend fun clearCart(): ApiResult<Cart>
}

