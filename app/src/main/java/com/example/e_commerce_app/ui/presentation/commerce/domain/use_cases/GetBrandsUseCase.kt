package com.example.e_commerce_app.ui.presentation.commerce.domain.use_cases

import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.common.CategoryItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.repository.home.HomeRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(val homeRepository: HomeRepository) {
    suspend fun invoke(): ApiResult<List<CategoryItem>> = homeRepository.getBrands()
}