package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.wishList

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.e_commerce_app.ui.presentation.wishList.feature.WishListScreen

@Composable
fun WishListTab(
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {}
) {
    WishListScreen(
        modifier = modifier,
        onProductClick = onProductClick
    )
}
