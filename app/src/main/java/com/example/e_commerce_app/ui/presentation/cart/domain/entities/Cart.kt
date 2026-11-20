package com.example.e_commerce_app.ui.presentation.cart.domain.entities

data class Cart(
    val id: String = "",
    val totalCartPrice: Int = 0,
    val numOfCartItems: Int = 0,
    val products: List<CartProductItem> = emptyList()
)

data class CartProductItem(
    val id: String = "",
    val count: Int = 0,
    val price: Int = 0,
    val product: CartProduct = CartProduct()
)

data class CartProduct(
    val id: String = "",
    val title: String = "",
    val imageCover: String = "",
    val ratingsAverage: Double = 0.0,
    val categoryName: String = "",
    val brandName: String = ""
)

