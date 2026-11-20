package com.example.e_commerce_app.ui.presentation.network.api

import com.example.e_commerce_app.ui.presentation.network.models.request.auth.login.LoginRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.request.auth.register.RegisterRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.request.cart.AddToCartRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.request.cart.UpdateCartQuantityRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.request.wishlist.AddWishlistRequestDto
import com.example.e_commerce_app.ui.presentation.network.models.response.auth.AuthResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.AddToCartResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.category.CategoryResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.products.ProductResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.wishlist.GetWishlistResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.wishlist.WishlistActionResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServices {
    @POST(ApiEndPoints.LOGIN)
    suspend fun login(@Body loginRequest: LoginRequestDto) : AuthResponseDto

    @POST(ApiEndPoints.REGISTER)
    suspend fun register(@Body registerRequest: RegisterRequestDto) : AuthResponseDto

    @GET(ApiEndPoints.CATEGORIES)
    suspend fun getAllCategories() : CategoryResponseDto

    @GET(ApiEndPoints.BRANDS)
    suspend fun getAllBrands() : CategoryResponseDto

    @GET(ApiEndPoints.PRODUCTS)
    suspend fun getAllProducts(): ProductResponseDto

    @GET(ApiEndPoints.CART)
    suspend fun getCart(@Header("token") token: String): CartResponseDto

    @POST(ApiEndPoints.CART)
    suspend fun addToCart(
        @Header("token") token: String,
        @Body body: AddToCartRequestDto
    ): AddToCartResponseDto

    @PUT("${ApiEndPoints.CART}/{productId}")
    suspend fun updateCartQuantity(
        @Header("token") token: String,
        @Path("productId") productId: String,
        @Body body: UpdateCartQuantityRequestDto
    ): CartResponseDto

    @DELETE("${ApiEndPoints.CART}/{productId}")
    suspend fun removeCartItem(
        @Header("token") token: String,
        @Path("productId") productId: String
    ): CartResponseDto

    @DELETE(ApiEndPoints.CART)
    suspend fun clearCart(@Header("token") token: String): CartResponseDto

    @GET(ApiEndPoints.WISHLIST)
    suspend fun getWishlist(@Header("token") token: String): GetWishlistResponseDto

    @POST(ApiEndPoints.WISHLIST)
    suspend fun addToWishlist(
        @Header("token") token: String,
        @Body body: AddWishlistRequestDto
    ): WishlistActionResponseDto

    @DELETE("${ApiEndPoints.WISHLIST}/{productId}")
    suspend fun removeFromWishlist(
        @Header("token") token: String,
        @Path("productId") productId: String
    ): WishlistActionResponseDto
}