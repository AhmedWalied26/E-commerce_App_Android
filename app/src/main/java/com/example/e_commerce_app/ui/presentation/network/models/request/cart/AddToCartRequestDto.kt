package com.example.e_commerce_app.ui.presentation.network.models.request.cart

import com.google.gson.annotations.SerializedName

data class AddToCartRequestDto(
    @field:SerializedName("productId")
    val productId: String
)

