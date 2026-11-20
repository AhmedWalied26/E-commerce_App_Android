package com.example.e_commerce_app.ui.presentation.commerce.domain.use_cases

import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.repository.home.HomeRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(productId: String): ProductItem? =
        homeRepository.getProductById(productId)
}

