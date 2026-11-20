package com.example.e_commerce_app.ui.presentation.commerce.domain.repository.home

import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.common.CategoryItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult

interface HomeRepository {
    suspend fun getCategory(): ApiResult<List<CategoryItem>>
    suspend fun getBrands(): ApiResult<List<CategoryItem>>

    suspend fun getProducts(): ApiResult<List<ProductItem>>
    fun getProductById(productId: String): ProductItem?
}