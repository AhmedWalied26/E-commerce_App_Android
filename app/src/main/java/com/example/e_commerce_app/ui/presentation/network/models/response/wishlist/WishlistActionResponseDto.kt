package com.example.e_commerce_app.ui.presentation.network.models.response.wishlist

import com.google.gson.annotations.SerializedName

data class WishlistActionResponseDto(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<String>? = null
)

