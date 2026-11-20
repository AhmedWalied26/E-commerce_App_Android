package com.example.e_commerce_app.ui.presentation.commerce.data.data_sources.remote.home

import com.example.e_commerce_app.ui.presentation.network.api.ApiServices
import com.example.e_commerce_app.ui.presentation.network.models.response.category.CategoryResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.products.ProductResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.network.utilty.safeApiCall
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(val apiServices: ApiServices) :
    HomeRemoteDataSource {
    override suspend fun getCategory(): ApiResult<CategoryResponseDto> {
        return safeApiCall {
            apiServices.getAllCategories()
        }
    }

    override suspend fun getBrands(): ApiResult<CategoryResponseDto> {
        return safeApiCall {
            apiServices.getAllBrands()
        }
    }

    override suspend fun getProducts(): ApiResult<ProductResponseDto> {
        return safeApiCall {
            apiServices.getAllProducts()
        }
    }
}