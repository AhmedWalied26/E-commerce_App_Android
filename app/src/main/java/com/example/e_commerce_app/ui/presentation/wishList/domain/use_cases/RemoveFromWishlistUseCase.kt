package com.example.e_commerce_app.ui.presentation.wishList.domain.use_cases

import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.wishList.domain.repository.WishlistRepository
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(productId: String): ApiResult<Unit> {
        return wishlistRepository.removeFromWishlist(productId)
    }
}

