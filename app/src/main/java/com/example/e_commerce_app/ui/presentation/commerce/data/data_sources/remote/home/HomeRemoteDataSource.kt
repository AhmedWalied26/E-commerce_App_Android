package com.example.e_commerce_app.ui.presentation.commerce.data.data_sources.remote.home

import com.example.e_commerce_app.ui.presentation.network.models.response.category.CategoryResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.products.ProductResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult

interface HomeRemoteDataSource {
    suspend fun getCategory(): ApiResult<CategoryResponseDto>
    suspend fun getBrands(): ApiResult<CategoryResponseDto>

    suspend fun getProducts(): ApiResult<ProductResponseDto>
}