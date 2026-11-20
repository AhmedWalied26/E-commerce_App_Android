package com.example.e_commerce_app.ui.presentation.commerce.data.mappers

import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.network.models.response.products.ProductItemDto

fun ProductItemDto.toProductItem() : ProductItem{
    return ProductItem(
        images = images,
        quantity = quantity,
        imageCover = imageCover,
        description = description,
        title = title,
        ratingsQuantity = ratingsQuantity,
        ratingsAverage = ratingsAverage,
        price = price,
        id = id,
        subcategory = subcategory,
        category = category,
        brand = brand,
        priceAfterDiscount = priceAfterDiscount,
        sold = sold,
    )
}