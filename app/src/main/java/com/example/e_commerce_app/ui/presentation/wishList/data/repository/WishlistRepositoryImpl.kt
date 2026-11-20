package com.example.e_commerce_app.ui.presentation.wishList.data.repository

import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.wishList.data.data_sources.remote.WishlistRemoteDataSource
import com.example.e_commerce_app.ui.presentation.wishList.data.mappers.toWishlistItem
import com.example.e_commerce_app.ui.presentation.wishList.domain.entities.WishlistItem
import com.example.e_commerce_app.ui.presentation.wishList.domain.repository.WishlistRepository
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val remoteDataSource: WishlistRemoteDataSource
) : WishlistRepository {

    override suspend fun getWishlist(): ApiResult<List<WishlistItem>> {
        return when (val result = remoteDataSource.getWishlist()) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(result.error)
            is ApiResult.SuccessApiResult -> {
                val items = result.data.data.orEmpty().map { it.toWishlistItem() }
                ApiResult.SuccessApiResult(items)
            }
        }
    }

    override suspend fun addToWishlist(productId: String): ApiResult<Unit> {
        return when (val result = remoteDataSource.addToWishlist(productId)) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(result.error)
            is ApiResult.SuccessApiResult -> ApiResult.SuccessApiResult(Unit)
        }
    }

    override suspend fun removeFromWishlist(productId: String): ApiResult<Unit> {
        return when (val result = remoteDataSource.removeFromWishlist(productId)) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(result.error)
            is ApiResult.SuccessApiResult -> ApiResult.SuccessApiResult(Unit)
        }
    }
}

