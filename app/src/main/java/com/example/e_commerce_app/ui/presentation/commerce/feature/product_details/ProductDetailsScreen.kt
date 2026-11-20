package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components.BottomAddToCartSection
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components.ColorSelector
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components.ProductDescriptionSection
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components.ProductImageCarousel
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components.ProductInfoSection
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components.SizeSelector
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.view_model.ProductDetailsEffects
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.view_model.ProductDetailsIntent
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.view_model.ProductDetailsViewModel
import com.example.e_commerce_app.ui.presentation.common.MainErrorSection
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String = "",
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToCart: () -> Unit = {}
) {
    val state by viewModel.states.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(productId) {
        if (productId.isNotBlank()) {
            viewModel.processIntent(ProductDetailsIntent.LoadProductDetails(productId))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ProductDetailsEffects.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is ProductDetailsEffects.NavigateBack -> onBackClick()
                is ProductDetailsEffects.NavigateToCart -> onNavigateToCart()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Product Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkBlue
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkBlue
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "Search",
                            tint = DarkBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(onClick = onNavigateToCart) {
                        BadgedBox(
                            badge = {
                                if (state.cartCount > 0) {
                                    Badge(
                                        containerColor = Blue,
                                        contentColor = White
                                    ) {
                                        Text(text = state.cartCount.toString())
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    scrolledContainerColor = White
                )
            )
        },
        bottomBar = {
            if (state.productDetailsState is Resource.Success) {
                val product = (state.productDetailsState as Resource.Success).data
                val unitPrice = product.priceAfterDiscount ?: product.price ?: 0
                val totalPrice = unitPrice * state.selectedQuantity
                BottomAddToCartSection(
                    totalPrice = totalPrice,
                    isAddingToCart = state.isAddingToCart,
                    onAddToCartClick = {
                        viewModel.processIntent(ProductDetailsIntent.AddToCart)
                    }
                )
            }
        },
        containerColor = White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(White)
        ) {
            when (val productState = state.productDetailsState) {
                is Resource.Loading, Resource.Initial -> MainLoadingSection()
                is Resource.Error -> MainErrorSection(
                    errorMessage = productState.errorMessage,
                    buttonText = "Back",
                    onTryAgain = onBackClick
                )
                is Resource.Success -> {
                    val product = productState.data
                    val images = product.images?.filterNotNull()
                        ?.ifEmpty { listOfNotNull(product.imageCover) }
                        ?: listOfNotNull(product.imageCover)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        ProductImageCarousel(
                            images = images,
                            isFavorite = state.isFavorite,
                            isFavoriteLoading = state.isFavoriteLoading,
                            onFavoriteClick = {
                                viewModel.processIntent(ProductDetailsIntent.ToggleWishlist)
                            }
                        )

                        ProductInfoSection(
                            product = product,
                            selectedQuantity = state.selectedQuantity,
                            onIncrementQuantity = {
                                viewModel.processIntent(ProductDetailsIntent.IncrementQuantity)
                            },
                            onDecrementQuantity = {
                                viewModel.processIntent(ProductDetailsIntent.DecrementQuantity)
                            }
                        )

                        if (!product.description.isNullOrBlank()) {
                            ProductDescriptionSection(
                                description = product.description,
                                isExpanded = state.isDescriptionExpanded,
                                onToggleExpand = {
                                    viewModel.processIntent(ProductDetailsIntent.ToggleDescriptionExpanded)
                                }
                            )
                        }

                        SizeSelector(
                            selectedSize = state.selectedSize,
                            onSizeSelected = {
                                viewModel.processIntent(ProductDetailsIntent.SelectSize(it))
                            }
                        )

                        ColorSelector(
                            selectedColorIndex = state.selectedColorIndex,
                            onColorSelected = {
                                viewModel.processIntent(ProductDetailsIntent.SelectColor(it))
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

