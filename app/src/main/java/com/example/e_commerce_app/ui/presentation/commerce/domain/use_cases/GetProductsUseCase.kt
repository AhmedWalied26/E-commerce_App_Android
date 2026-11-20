package com.example.e_commerce_app.ui.presentation.commerce.domain.use_cases

import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.repository.home.HomeRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(val homeRepository: HomeRepository) {
    suspend fun invoke(): ApiResult<List<ProductItem>> = homeRepository.getProducts()
}