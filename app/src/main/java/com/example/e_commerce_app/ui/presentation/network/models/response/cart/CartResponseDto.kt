package com.example.e_commerce_app.ui.presentation.network.models.response.cart

import com.example.e_commerce_app.ui.presentation.network.models.response.common.CategoryItemDto
import com.google.gson.annotations.SerializedName

data class CartResponseDto(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("numOfCartItems")
    val numOfCartItems: Int? = null,

    @field:SerializedName("cartId")
    val cartId: String? = null,

    @field:SerializedName("data")
    val data: CartDataDto? = null
)

data class CartDataDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("cartOwner")
    val cartOwner: String? = null,

    @field:SerializedName("products")
    val products: List<CartProductItemDto>? = null,

    @field:SerializedName("totalCartPrice")
    val totalCartPrice: Int? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class CartProductItemDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("product")
    val product: CartProductDto? = null
)

data class CartProductDto(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("id")
    val idAlias: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("imageCover")
    val imageCover: String? = null,

    @field:SerializedName("category")
    val category: CategoryItemDto? = null,

    @field:SerializedName("brand")
    val brand: CategoryItemDto? = null,

    @field:SerializedName("ratingsAverage")
    val ratingsAverage: Double? = null,

    @field:SerializedName("subcategory")
    val subcategory: List<CategoryItemDto>? = null
)

