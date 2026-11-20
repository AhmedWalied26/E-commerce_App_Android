package com.example.e_commerce_app.ui.presentation.cart.data.repository

import com.example.e_commerce_app.ui.core.errors.AppErrors
import com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote.CartRemoteDataSource
import com.example.e_commerce_app.ui.presentation.cart.data.mappers.toCart
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.repository.CartRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {

    private val _cartCount = MutableStateFlow(0)
    override val cartCount: StateFlow<Int> = _cartCount.asStateFlow()

    override suspend fun getCart(): ApiResult<Cart> {
        return when (val result = cartRemoteDataSource.getCart()) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(message = result.error.errorMessage)
            )
            is ApiResult.SuccessApiResult -> {
                val cart = result.data.toCart()
                // Initial Launch (GET): initialize cartCount with numOfCartItems
                _cartCount.value = cart.numOfCartItems
                ApiResult.SuccessApiResult(data = cart)
            }
        }
    }

    override suspend fun addToCart(productId: String): ApiResult<Unit> {
        return when (val result = cartRemoteDataSource.addToCart(productId)) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(message = result.error.errorMessage)
            )
            is ApiResult.SuccessApiResult -> {
                // Add Only (POST): Update cartCount with new numOfCartItems
                val newCount = result.data.numOfCartItems ?: (_cartCount.value + 1)
                _cartCount.value = newCount
                ApiResult.SuccessApiResult(data = Unit)
            }
        }
    }

    override suspend fun updateProductQuantity(productId: String, count: Int): ApiResult<Cart> {
        return when (val result = cartRemoteDataSource.updateCartQuantity(productId, count)) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(message = result.error.errorMessage)
            )
            is ApiResult.SuccessApiResult -> {
                // Quantity Updates (PUT): Do NOT alter cartCount
                val cart = result.data.toCart()
                ApiResult.SuccessApiResult(data = cart)
            }
        }
    }

    override suspend fun removeCartItem(productId: String): ApiResult<Cart> {
        return when (val result = cartRemoteDataSource.removeCartItem(productId)) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(message = result.error.errorMessage)
            )
            is ApiResult.SuccessApiResult -> {
                val cart = result.data.toCart()
                // Remove Only (DELETE item): Update cartCount with new numOfCartItems
                _cartCount.value = cart.numOfCartItems
                ApiResult.SuccessApiResult(data = cart)
            }
        }
    }

    override suspend fun clearCart(): ApiResult<Cart> {
        return when (val result = cartRemoteDataSource.clearCart()) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(message = result.error.errorMessage)
            )
            is ApiResult.SuccessApiResult -> {
                _cartCount.value = 0
                ApiResult.SuccessApiResult(data = result.data.toCart())
            }
        }
    }
}

