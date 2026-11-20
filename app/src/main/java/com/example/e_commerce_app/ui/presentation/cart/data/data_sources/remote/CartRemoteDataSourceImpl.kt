package com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote

import com.example.e_commerce_app.ui.presentation.auth.data.data_source.local.AuthLocalDataSource
import com.example.e_commerce_app.ui.presentation.network.api.ApiServices
import com.example.e_commerce_app.ui.presentation.network.models.request.cart.AddToCartRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.request.cart.UpdateCartQuantityRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.AddToCartResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.network.utilty.safeApiCall
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CartRemoteDataSourceImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val authLocalDataSource: AuthLocalDataSource
) : CartRemoteDataSource {

    private suspend fun getToken(): String {
        return authLocalDataSource.tokenFlow.first().orEmpty()
    }

    override suspend fun getCart(): ApiResult<CartResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.getCart(token)
        }
    }

    override suspend fun addToCart(productId: String): ApiResult<AddToCartResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.addToCart(token, AddToCartRequestDto(productId))
        }
    }

    override suspend fun updateCartQuantity(productId: String, count: Int): ApiResult<CartResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.updateCartQuantity(token, productId, UpdateCartQuantityRequestDto(count))
        }
    }

    override suspend fun removeCartItem(productId: String): ApiResult<CartResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.removeCartItem(token, productId)
        }
    }

    override suspend fun clearCart(): ApiResult<CartResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.clearCart(token)
        }
    }
}

