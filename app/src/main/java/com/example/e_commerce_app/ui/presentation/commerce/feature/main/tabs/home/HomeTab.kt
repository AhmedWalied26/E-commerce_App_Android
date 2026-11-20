package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.core.components.ImageSlider
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.common.CategoryItem
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.home.composable.HomeSuccessSection
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.home.view_model.HomeIntent
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.home.view_model.HomeViewModel
import com.example.e_commerce_app.ui.presentation.common.MainErrorSection
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.theme.medium18DarkBlue
import com.example.e_commerce_app.ui.theme.regular12DarkBlue

@Composable
fun HomeTab(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.states.collectAsState()
    val imageList = listOf(
        R.drawable.offers_image1, R.drawable.offers_image2, R.drawable.offers_image3
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {

        item {
            Spacer(modifier = Modifier.height(16.dp))

            ImageSlider(images = imageList, modifier = Modifier.padding(16.dp))
        }

        item {

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Categories", style = medium18DarkBlue
                )
                Text(
                    text = "view all", style = regular12DarkBlue
                )
            }
        }

        item {
            when (val categoryState = state.categoryState) {
                is Resource.Error -> MainErrorSection(
                    onTryAgain = { viewModel.processIntent(HomeIntent.LoadCategory) },
                    errorMessage = categoryState.errorMessage
                )

                Resource.Initial -> MainLoadingSection()
                Resource.Loading -> MainLoadingSection()
                is Resource.Success<List<CategoryItem>> -> HomeSuccessSection(
                    list = categoryState.data
                )
            }
        }

        item {

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Brands", style = medium18DarkBlue
                )
                Text(
                    text = "view all", style = regular12DarkBlue
                )
            }
        }

        item {
            when (val brandState = state.brandState) {
                is Resource.Error -> MainErrorSection(
                    onTryAgain = { viewModel.processIntent(HomeIntent.LoadBrand) },
                    errorMessage = brandState.errorMessage
                )

                Resource.Initial -> MainLoadingSection()
                Resource.Loading -> MainLoadingSection()
                is Resource.Success<List<CategoryItem>> -> HomeSuccessSection(
                    list = brandState.data
                )
            }
        }
    }
}