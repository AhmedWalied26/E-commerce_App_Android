package com.example.e_commerce_app.ui.presentation.wishList.domain.repository

import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.wishList.domain.entities.WishlistItem

interface WishlistRepository {
    suspend fun getWishlist(): ApiResult<List<WishlistItem>>
    suspend fun addToWishlist(productId: String): ApiResult<Unit>
    suspend fun removeFromWishlist(productId: String): ApiResult<Unit>
}

