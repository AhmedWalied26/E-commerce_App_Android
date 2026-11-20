package com.example.e_commerce_app.ui.presentation.cart.data.mappers

import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.CartProduct
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.CartProductItem
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartProductDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartProductItemDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartResponseDto

fun CartResponseDto.toCart(): Cart {
    return Cart(
        id = data?.id ?: cartId.orEmpty(),
        totalCartPrice = data?.totalCartPrice ?: 0,
        numOfCartItems = numOfCartItems ?: data?.products?.size ?: 0,
        products = data?.products?.map { it.toCartProductItem() }.orEmpty()
    )
}

fun CartProductItemDto.toCartProductItem(): CartProductItem {
    return CartProductItem(
        id = id.orEmpty(),
        count = count ?: 0,
        price = price ?: 0,
        product = product?.toCartProduct() ?: CartProduct()
    )
}

fun CartProductDto.toCartProduct(): CartProduct {
    return CartProduct(
        id = id ?: idAlias.orEmpty(),
        title = title.orEmpty(),
        imageCover = imageCover.orEmpty(),
        ratingsAverage = ratingsAverage ?: 0.0,
        categoryName = category?.name.orEmpty(),
        brandName = brand?.name.orEmpty()
    )
}

