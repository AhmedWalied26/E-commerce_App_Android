package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.home.view_model

import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.common.CategoryItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource

data class HomeStates(
    val categoryState: Resource<List<CategoryItem>> = Resource.Initial,
    val brandState: Resource<List<CategoryItem>> = Resource.Initial
) : UiStates
