package com.example.e_commerce_app.ui.presentation.network.models.request.wishlist

import com.google.gson.annotations.SerializedName

data class AddWishlistRequestDto(
    @field:SerializedName("productId")
    val productId: String
)

