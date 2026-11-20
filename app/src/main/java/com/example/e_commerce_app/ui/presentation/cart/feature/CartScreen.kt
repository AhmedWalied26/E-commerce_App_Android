package com.example.e_commerce_app.ui.presentation.cart.feature

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.feature.components.CartBottomBar
import com.example.e_commerce_app.ui.presentation.cart.feature.components.CartItemCard
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartEffects
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartIntent
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartStates
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartViewModel
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.common.MainEmptyListSection
import com.example.e_commerce_app.ui.presentation.common.MainErrorSection
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.Gray
import com.example.e_commerce_app.ui.theme.White
import com.example.e_commerce_app.ui.theme.medium18DarkBlue
import com.example.e_commerce_app.ui.theme.regular14DarkBlue

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit = {},
    onNavigateToProductDetails: ((String) -> Unit)? = null,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.states.collectAsState()
    val context = LocalContext.current
    val navigateToDetails = onNavigateToProductDetails ?: onProductClick

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CartEffects.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                CartEffects.NavigateBack -> {
                    onBackClick()
                }

                CartEffects.NavigateToCheckout -> {
                    Toast.makeText(context, "Proceeding to checkout...", Toast.LENGTH_SHORT).show()
                }

                is CartEffects.NavigateToProductDetails -> {
                    navigateToDetails(effect.productId)
                }
            }
        }
    }

    CartContent(
        modifier = modifier,
        state = state,
        onIntent = viewModel::processIntent,
        onProductClick = navigateToDetails
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    modifier: Modifier = Modifier,
    state: CartStates,
    onIntent: (CartIntent) -> Unit,
    onProductClick: (String) -> Unit = {}
) {
    val cartCount = (state.cartState as? Resource.Success)?.data?.numOfCartItems ?: 0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                ),
                navigationIcon = {
                    IconButton(onClick = { onIntent(CartIntent.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkBlue
                        )
                    }
                },
                title = {
                    Text(
                        text = "Cart",
                        style = medium18DarkBlue,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.search_icon),
                                contentDescription = "Search",
                                tint = DarkBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        IconButton(onClick = {}) {
                            BadgedBox(
                                badge = {
                                    if (cartCount > 0) {
                                        Badge(
                                            containerColor = Blue,
                                            contentColor = White
                                        ) {
                                            Text(text = cartCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.cart_icon),
                                    contentDescription = "Cart",
                                    tint = DarkBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            val cartResource = state.cartState
            if (cartResource is Resource.Success && cartResource.data.products.isNotEmpty()) {
                CartBottomBar(
                    totalPrice = cartResource.data.totalCartPrice,
                    itemCount = cartCount,
                    onCheckOutClicked = { onIntent(CartIntent.OnCheckOutClicked) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val cartResource = state.cartState) {
                is Resource.Initial,
                is Resource.Loading -> {
                    MainLoadingSection()
                }

                is Resource.Error -> {
                    MainErrorSection(
                        errorMessage = cartResource.errorMessage,
                        onTryAgain = { onIntent(CartIntent.LoadCart) }
                    )
                }

                is Resource.Success<Cart> -> {
                    val cart = cartResource.data
                    if (cart.products.isEmpty()) {
                        MainEmptyListSection(
                            message = "Your Cart is Empty",
                            description = "Explore products and add items to your cart!"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(White),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$cartCount items in cart",
                                        style = regular14DarkBlue.copy(color = Gray)
                                    )
                                }
                            }

                            items(
                                items = cart.products,
                                key = { it.id.ifEmpty { it.product.id } }
                            ) { item ->
                                val productId = item.product.id.ifEmpty { item.id }
                                CartItemCard(
                                    item = item,
                                    isUpdating = state.updatingProductId == productId,
                                    onProductClick = {
                                        onProductClick(productId)
                                    },
                                    onIncrement = {
                                        onIntent(
                                            CartIntent.UpdateProductQuantity(
                                                productId = productId,
                                                count = item.count + 1
                                            )
                                        )
                                    },
                                    onDecrement = {
                                        onIntent(
                                            CartIntent.UpdateProductQuantity(
                                                productId = productId,
                                                count = item.count - 1
                                            )
                                        )
                                    },
                                    onDelete = {
                                        onIntent(
                                            CartIntent.RemoveProductFromCart(productId = productId)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
