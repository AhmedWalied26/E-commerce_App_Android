package com.example.e_commerce_app.ui.presentation.commerce.data.mappers

import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.common.CategoryItem
import com.example.e_commerce_app.ui.presentation.network.models.response.common.CategoryItemDto

fun CategoryItemDto.toCategory(): CategoryItem {
    return CategoryItem(
        image = image,
        name = name,
        id = id,
        category = category
    )
}