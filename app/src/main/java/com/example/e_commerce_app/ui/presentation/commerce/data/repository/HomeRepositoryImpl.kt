package com.example.e_commerce_app.ui.presentation.commerce.data.repository

import com.example.e_commerce_app.ui.core.errors.AppErrors
import com.example.e_commerce_app.ui.presentation.commerce.data.data_sources.remote.home.HomeRemoteDataSource
import com.example.e_commerce_app.ui.presentation.commerce.data.mappers.toCategory
import com.example.e_commerce_app.ui.presentation.commerce.data.mappers.toProductItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.common.CategoryItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.repository.home.HomeRepository
import com.example.e_commerce_app.ui.presentation.network.models.response.category.CategoryResponseDto
import com.example.e_commerce_app.ui.presentation.network.models.response.products.ProductResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(val homeRemoteDataSource: HomeRemoteDataSource) :
    HomeRepository {

    companion object {
        var cachedProducts: List<ProductItem> = emptyList()
    }
    override suspend fun getCategory(): ApiResult<List<CategoryItem>> {
        return when (val result = homeRemoteDataSource.getCategory()) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(
                    message = result.error.errorMessage
                )
            )

            is ApiResult.SuccessApiResult<CategoryResponseDto> ->
                ApiResult.SuccessApiResult(
                    data = result.data.data?.map { categoryItemDto ->
                        return@map categoryItemDto.toCategory()
                    } ?: emptyList()
                )
        }
    }

    override suspend fun getBrands(): ApiResult<List<CategoryItem>> {
        return when (val result = homeRemoteDataSource.getBrands()) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(
                    message = result.error.errorMessage
                )
            )

            is ApiResult.SuccessApiResult<CategoryResponseDto> ->
                ApiResult.SuccessApiResult(
                    data = result.data.data?.map { categoryItemDto ->
                        categoryItemDto.toCategory()
                    } ?: emptyList()
                )
        }
    }

    override suspend fun getProducts(): ApiResult<List<ProductItem>> {
        return when (val result = homeRemoteDataSource.getProducts()) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(
                    message = result.error.errorMessage
                )
            )

            is ApiResult.SuccessApiResult<ProductResponseDto> -> {
                val list = result.data.data?.map { productItemDto ->
                    productItemDto.toProductItem()
                } ?: emptyList()
                cachedProducts = list
                ApiResult.SuccessApiResult(data = list)
            }
        }
    }

    override fun getProductById(productId: String): ProductItem? {
        return cachedProducts.firstOrNull { it.id == productId }
    }
}