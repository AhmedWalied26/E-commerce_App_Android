package com.example.e_commerce_app.ui.presentation.commerce.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.ui.core.components.CustomAppBar
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.components.CustomBottomBar
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.home.HomeTab
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.ProductTab
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.profile.ProfileTab
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.wishList.WishListTab
import com.example.e_commerce_app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNavigateToCart: () -> Unit = {},
    onNavigateToProductDetails: (String) -> Unit = {},
    logout: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val state by viewModel.states.collectAsState()
    val selectedIndex = state.selectedTabIndex
    val cartCount by viewModel.cartCount.collectAsState()
    LaunchedEffect(selectedIndex) {
        scrollBehavior.state.heightOffset = 0f
        scrollBehavior.state.contentOffset = 0f
    }


    LaunchedEffect(Unit) {
        viewModel.effects.collect { effects ->
            when (effects) {
                MainScreenEffects.OnNavigateLogin -> logout()
            }
        }
    }

    Scaffold(
            modifier = modifier.nestedScroll(
                scrollBehavior.nestedScrollConnection
            ),
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(top = 30.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = White,
                        scrolledContainerColor = White
                    ),
                    scrollBehavior = scrollBehavior,
                    title = {
                        CustomAppBar(
                            cartCount = cartCount,
                            showSearch = selectedIndex != 3,
                            logoutClicked = {
                                viewModel.processIntent(MainScreenIntent.OnLogOutClicked)
                            },
                            cartClicked = onNavigateToCart
                        )
                    }
                )
            },
            bottomBar = {
                CustomBottomBar(
                    selectedIndex = selectedIndex,
                    onClickTab = { index ->
                        viewModel.processIntent(MainScreenIntent.SelectTab(index))
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(innerPadding)
            )
            {
                when (selectedIndex) {
                    0 -> HomeTab()
                    1 -> ProductTab(onProductClick = onNavigateToProductDetails)
                    2 -> WishListTab(onProductClick = onNavigateToProductDetails)
                    3 -> ProfileTab()
                }
            }
        }
}