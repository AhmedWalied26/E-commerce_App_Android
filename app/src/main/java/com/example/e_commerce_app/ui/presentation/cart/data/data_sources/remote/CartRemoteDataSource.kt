package com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote

import com.example.e_commerce_app.ui.presentation.network.models.response.cart.AddToCartResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult

interface CartRemoteDataSource {
    suspend fun getCart(): ApiResult<CartResponseDto>
    suspend fun addToCart(productId: String): ApiResult<AddToCartResponseDto>
    suspend fun updateCartQuantity(productId: String, count: Int): ApiResult<CartResponseDto>
    suspend fun removeCartItem(productId: String): ApiResult<CartResponseDto>
    suspend fun clearCart(): ApiResult<CartResponseDto>
}

