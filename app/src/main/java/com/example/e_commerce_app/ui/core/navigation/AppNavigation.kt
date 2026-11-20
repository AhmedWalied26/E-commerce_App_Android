package com.example.e_commerce_app.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.e_commerce_app.ui.presentation.auth.feature.login.LoginScreen
import com.example.e_commerce_app.ui.presentation.auth.feature.register.RegisterScreen
import com.example.e_commerce_app.ui.presentation.cart.feature.CartScreen
import com.example.e_commerce_app.ui.presentation.commerce.feature.main.MainScreen
import com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.ProductDetailsScreen
import com.example.e_commerce_app.ui.presentation.wishList.feature.WishListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: Any,
    onSplashFinished: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginClick = {
                    onSplashFinished()
                    navController.navigate(MainRoute) {
                        popUpTo(LoginRoute) {
                            inclusive = true
                        }
                    }
                }, onCreateAccount = {
                    navController.navigate(RegisterRoute)
                })
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onSignUpClick = {
                    navController.navigate(MainRoute) {
                        popUpTo(RegisterRoute) {
                            inclusive = true
                        }
                    }
                })
        }

        composable<MainRoute> {
            MainScreen(
                onNavigateToCart = {
                    navController.navigate(CartRoute)
                },
                onNavigateToProductDetails = { productId ->
                    navController.navigate(ProductDetailsRoute(productId))
                },
                logout = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<CartRoute> {
            CartScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(ProductDetailsRoute(productId))
                }
            )
        }

        composable<WishlistRoute> {
            WishListScreen(
                onProductClick = { productId ->
                    navController.navigate(ProductDetailsRoute(productId))
                }
            )
        }

        composable<ProductDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ProductDetailsRoute>()
            ProductDetailsScreen(
                productId = route.productId,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToCart = {
                    navController.navigate(CartRoute)
                }
            )
        }
    }
}