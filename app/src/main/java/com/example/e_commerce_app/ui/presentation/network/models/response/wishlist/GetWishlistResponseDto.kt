package com.example.e_commerce_app.ui.presentation.network.models.response.wishlist

import com.example.e_commerce_app.ui.presentation.network.models.response.products.ProductItemDto
import com.google.gson.annotations.SerializedName

data class GetWishlistResponseDto(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("data")
    val data: List<ProductItemDto>? = null
)

