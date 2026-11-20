package com.example.e_commerce_app.ui.presentation.wishList.data.data_sources.remote

import com.example.e_commerce_app.ui.presentation.auth.data.data_source.local.AuthLocalDataSource
import com.example.e_commerce_app.ui.presentation.network.api.ApiServices
import com.example.e_commerce_app.ui.presentation.network.models.request.wishlist.AddWishlistRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.response.wishlist.GetWishlistResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.wishlist.WishlistActionResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.network.utilty.safeApiCall
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WishlistRemoteDataSourceImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val authLocalDataSource: AuthLocalDataSource
) : WishlistRemoteDataSource {

    private suspend fun getToken(): String {
        return authLocalDataSource.tokenFlow.first().orEmpty()
    }

    override suspend fun getWishlist(): ApiResult<GetWishlistResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.getWishlist(token)
        }
    }

    override suspend fun addToWishlist(productId: String): ApiResult<WishlistActionResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.addToWishlist(token, AddWishlistRequestDto(productId))
        }
    }

    override suspend fun removeFromWishlist(productId: String): ApiResult<WishlistActionResponseDto> {
        val token = getToken()
        return safeApiCall {
            apiServices.removeFromWishlist(token, productId)
        }
    }
}

