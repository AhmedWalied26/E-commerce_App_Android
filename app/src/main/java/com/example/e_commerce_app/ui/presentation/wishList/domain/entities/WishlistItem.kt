package com.example.e_commerce_app.ui.presentation.wishList.domain.entities

data class WishlistItem(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val priceAfterDiscount: Int? = null,
    val imageCover: String,
    val ratingsAverage: Double = 0.0,
    val categoryName: String? = null,
    val brandName: String? = null,
    val colorPresentation: String? = null
)

