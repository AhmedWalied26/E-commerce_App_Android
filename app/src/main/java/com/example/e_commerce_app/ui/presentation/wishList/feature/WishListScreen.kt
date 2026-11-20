package com.example.e_commerce_app.ui.presentation.wishList.feature

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.common.MainEmptyListSection
import com.example.e_commerce_app.ui.presentation.common.MainErrorSection
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.presentation.wishList.feature.components.WishlistItemCard
import com.example.e_commerce_app.ui.presentation.wishList.domain.entities.WishlistItem
import com.example.e_commerce_app.ui.presentation.wishList.feature.view_model.WishListEffects
import com.example.e_commerce_app.ui.presentation.wishList.feature.view_model.WishListIntent
import com.example.e_commerce_app.ui.presentation.wishList.feature.view_model.WishListStates
import com.example.e_commerce_app.ui.presentation.wishList.feature.view_model.WishListViewModel
import com.example.e_commerce_app.ui.theme.White

@Composable
fun WishListScreen(
    modifier: Modifier = Modifier,
    viewModel: WishListViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit = {},
    onNavigateToProductDetails: ((String) -> Unit)? = null
) {
    val state by viewModel.states.collectAsState()
    val context = LocalContext.current
    val navigateToDetails = onNavigateToProductDetails ?: onProductClick

    LaunchedEffect(Unit) {
        viewModel.processIntent(WishListIntent.LoadWishlist)
        viewModel.effects.collect { effect ->
            when (effect) {
                is WishListEffects.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is WishListEffects.NavigateToProductDetails -> {
                    navigateToDetails(effect.productId)
                }
            }
        }
    }

    WishListContent(
        modifier = modifier,
        state = state,
        onIntent = viewModel::processIntent,
        onProductClick = navigateToDetails
    )
}

@Composable
fun WishListContent(
    modifier: Modifier = Modifier,
    state: WishListStates,
    onIntent: (WishListIntent) -> Unit,
    onProductClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        when (val res = state.wishlistState) {
            Resource.Initial,
            Resource.Loading -> {
                MainLoadingSection()
            }

            is Resource.Error -> {
                MainErrorSection(
                    errorMessage = res.errorMessage,
                    onTryAgain = { onIntent(WishListIntent.LoadWishlist) }
                )
            }

            is Resource.Success<List<WishlistItem>> -> {
                val items = res.data
                if (items.isEmpty()) {
                    MainEmptyListSection(
                        message = "Your Wishlist is Empty",
                        description = "Explore products and tap the heart icon to save items here!"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = items,
                            key = { it.id }
                        ) { item ->
                            WishlistItemCard(
                                item = item,
                                isRemoving = state.removingProductId == item.id,
                                isAddingToCart = state.addingToCartProductId == item.id,
                                onProductClick = {
                                    onProductClick(item.id)
                                },
                                onRemoveClick = {
                                    onIntent(WishListIntent.RemoveFromWishlist(item.id))
                                },
                                onAddToCartClick = {
                                    onIntent(WishListIntent.AddToCart(item.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
