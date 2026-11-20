package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.home.view_model

import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.core.mvi.UiEffects
import com.example.e_commerce_app.ui.core.mvi.UiIntent
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.common.CategoryItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.use_cases.GetBrandsUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.use_cases.GetCategoriesUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeIntent : UiIntent {
    data object LoadCategory : HomeIntent
    data object LoadBrand : HomeIntent
}

sealed interface HomeEffects : UiEffects {
    data class ShowToast(val message: String) : HomeEffects
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getCategoriesUseCase: GetCategoriesUseCase,
    val getBrandsUseCase: GetBrandsUseCase
) : BaseViewModel<HomeStates, HomeIntent, HomeEffects>(HomeStates()) {

    init {
        processIntent(HomeIntent.LoadCategory)
        processIntent(HomeIntent.LoadBrand)
    }

    override fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadBrand -> loadBrands()
            HomeIntent.LoadCategory -> loadCategory()
        }
    }

    private fun loadCategory() {
        viewModelScope.launch {
            updateState {
                copy(categoryState = Resource.Loading)
            }
            when (val result = getCategoriesUseCase.invoke()) {
                is ApiResult.ErrorApiResult -> updateState {
                    copy(categoryState = Resource.Error(errorMessage = result.error.errorMessage))
                }

                is ApiResult.SuccessApiResult<List<CategoryItem>> -> updateState {
                    copy(categoryState = Resource.Success(data = result.data))
                }
            }
        }
    }

    private fun loadBrands() {
        viewModelScope.launch {
            updateState {
                copy(brandState = Resource.Loading)
            }

            when (val result = getBrandsUseCase.invoke()) {
                is ApiResult.ErrorApiResult -> updateState {
                    copy(brandState = Resource.Error(errorMessage = result.error.errorMessage))
                }

                is ApiResult.SuccessApiResult<List<CategoryItem>> -> updateState {
                    copy(brandState = Resource.Success(data = result.data))
                }
            }
        }
    }
}
