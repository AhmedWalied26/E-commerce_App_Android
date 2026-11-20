package com.example.e_commerce_app.ui.presentation.wishList.data.data_sources.remote

import com.example.e_commerce_app.ui.presentation.network.models.response.wishlist.GetWishlistResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.wishlist.WishlistActionResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult

interface WishlistRemoteDataSource {
    suspend fun getWishlist(): ApiResult<GetWishlistResponseDto>
    suspend fun addToWishlist(productId: String): ApiResult<WishlistActionResponseDto>
    suspend fun removeFromWishlist(productId: String): ApiResult<WishlistActionResponseDto>
}

