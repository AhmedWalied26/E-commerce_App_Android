package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem as ProductItemEntity
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.components.ProductItem
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.view_model.ProductEffects
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.view_model.ProductIntent
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.view_model.ProductViewModel
import com.example.e_commerce_app.ui.presentation.common.MainErrorSection
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection

@Composable
fun ProductTab(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit = {}
) {
    val state by viewModel.states.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.processIntent(ProductIntent.RefreshWishlist)
        viewModel.effects.collect { effects ->
            when (effects) {
                is ProductEffects.ShowToast -> Toast.makeText(
                    context,
                    effects.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    when (val productState = state.productState) {
        is Resource.Error ->
            MainErrorSection(
                onTryAgain = {
                    viewModel.processIntent(ProductIntent.LoadProducts)
                },
                errorMessage = productState.errorMessage
            )

        Resource.Initial,
        Resource.Loading ->
            MainLoadingSection()

        is Resource.Success<List<ProductItemEntity>> -> LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = productState.data,
                key = { it.id.orEmpty() }
            ) { product ->
                val productId = product.id.orEmpty()
                ProductItem(
                    product = product,
                    isFavorite = state.favoriteProductIds.contains(productId),
                    isFavoriteLoading = state.wishlistLoadingProductIds.contains(productId),
                    onProductClick = { onProductClick(productId) },
                    onFavoriteClick = {
                        viewModel.processIntent(ProductIntent.ToggleWishlist(productId))
                    },
                    onAddToCartClick = {
                        viewModel.processIntent(
                            ProductIntent.AddToCart(productId = productId)
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}