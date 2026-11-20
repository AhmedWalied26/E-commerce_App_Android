package com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products

import com.example.e_commerce_app.ui.presentation.network.models.response.common.CategoryItemDto

data class ProductItem(

	val images: List<String?>? = null,

	val quantity: Int? = null,

	val imageCover: String? = null,

	val description: String? = null,

	val title: String? = null,

	val ratingsQuantity: Int? = null,

	val ratingsAverage: Any? = null,

	val price: Int? = null,

	val id: String? = null,

	val subcategory: List<CategoryItemDto?>? = null,

	val category: CategoryItemDto? = null,

	val brand: CategoryItemDto? = null,

	val priceAfterDiscount: Int? = null,

	val sold: Int? = null
)