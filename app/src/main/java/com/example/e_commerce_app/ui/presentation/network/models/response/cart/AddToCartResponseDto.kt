package com.example.e_commerce_app.ui.presentation.network.models.response.cart

import com.google.gson.annotations.SerializedName

data class AddToCartResponseDto(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("numOfCartItems")
    val numOfCartItems: Int? = null,

    @field:SerializedName("cartId")
    val cartId: String? = null,

    @field:SerializedName("data")
    val data: AddToCartDataDto? = null
)

data class AddToCartDataDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("cartOwner")
    val cartOwner: String? = null,

    @field:SerializedName("products")
    val products: List<AddToCartProductItemDto>? = null,

    @field:SerializedName("totalCartPrice")
    val totalCartPrice: Int? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class AddToCartProductItemDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("product")
    val product: String? = null
)

