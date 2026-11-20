package com.example.e_commerce_app.ui.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object MainRoute
@Serializable
object RegisterRoute
@Serializable
object LoginRoute
@Serializable
object CartRoute
@Serializable
object WishlistRoute
@Serializable
data class ProductDetailsRoute(val productId: String)