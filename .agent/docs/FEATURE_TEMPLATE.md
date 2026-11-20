# Feature Implementation Starter Template & Checklist

This document provides a complete, copy-pasteable blueprint for implementing any new feature in the codebase (e.g. `cart`, `wishlist`, `orders`, `profile`). Replace `[Feature]` / `[Model]` placeholders with your actual feature name.

---

## 1. Network Layer (Centralized)

### A. Endpoint in `ui/presentation/network/api/ApiEndPoints.kt`
```kotlin
object ApiEndPoints {
    // ... existing endpoints
    const val CART = "cart"
}
```

### B. Retrofit Declaration in `ui/presentation/network/api/ApiServices.kt`
```kotlin
interface ApiServices {
    // ... existing services
    @GET(ApiEndPoints.CART)
    suspend fun getCart(): CartResponseDto

    @POST(ApiEndPoints.CART)
    suspend fun addToCart(@Body body: AddToCartRequestDto): CartMutationResponseDto
}
```

### C. DTOs in `ui/presentation/network/models/response/cart/`
> **Rule Reminder:** Keep raw ID responses separate from expanded model responses!

```kotlin
package com.example.e_commerce_app.ui.presentation.network.models.response.cart

import com.google.gson.annotations.SerializedName

data class CartResponseDto(
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("numOfCartItems")
    val numOfCartItems: Int? = null,
    @field:SerializedName("data")
    val data: CartDataDto? = null
)

data class CartDataDto(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("totalCartPrice")
    val totalCartPrice: Int? = null,
    @field:SerializedName("products")
    val products: List<CartItemDto>? = null
)

data class CartItemDto(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("count")
    val count: Int? = null,
    @field:SerializedName("price")
    val price: Int? = null,
    @field:SerializedName("product")
    val product: CartProductDto? = null
)

data class CartProductDto(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("title")
    val title: String? = null,
    @field:SerializedName("imageCover")
    val imageCover: String? = null
)
```

---

## 2. Domain Layer (Pure Kotlin)

### A. Domain Entities in `ui/presentation/[feature]/domain/entities/`
```kotlin
package com.example.e_commerce_app.ui.presentation.cart.domain.entities

data class Cart(
    val id: String,
    val totalCartPrice: Int,
    val numOfCartItems: Int,
    val items: List<CartItem>
)

data class CartItem(
    val id: String,
    val count: Int,
    val price: Int,
    val productId: String,
    val productTitle: String,
    val productImage: String
)
```

### B. Repository Contract in `ui/presentation/[feature]/domain/repository/`
```kotlin
package com.example.e_commerce_app.ui.presentation.cart.domain.repository

import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult

interface CartRepository {
    suspend fun getCart(): ApiResult<Cart>
}
```

### C. UseCase in `ui/presentation/[feature]/domain/use_cases/`
```kotlin
package com.example.e_commerce_app.ui.presentation.cart.domain.use_cases

import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.repository.CartRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): ApiResult<Cart> = cartRepository.getCart()
}
```

---

## 3. Data Layer

### A. Mapper in `ui/presentation/[feature]/data/mappers/CartMapper.kt`
> **Rule Reminder:** Name mapper functions after the target domain model (e.g., `toCart()`), never `toDomain()`.

```kotlin
package com.example.e_commerce_app.ui.presentation.cart.data.mappers

import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.CartItem
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartItemDto
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartResponseDto

fun CartResponseDto.toCart(): Cart {
    return Cart(
        id = data?.id.orEmpty(),
        totalCartPrice = data?.totalCartPrice ?: 0,
        numOfCartItems = numOfCartItems ?: 0,
        items = data?.products?.map { it.toCartItem() }.orEmpty()
    )
}

fun CartItemDto.toCartItem(): CartItem {
    return CartItem(
        id = id.orEmpty(),
        count = count ?: 0,
        price = price ?: 0,
        productId = product?.id.orEmpty(),
        productTitle = product?.title.orEmpty(),
        productImage = product?.imageCover.orEmpty()
    )
}
```

### B. Remote Data Source Interface & Impl in `ui/presentation/[feature]/data/data_sources/remote/`
```kotlin
// CartRemoteDataSource.kt
package com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote

import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult

interface CartRemoteDataSource {
    suspend fun getCart(): ApiResult<CartResponseDto>
}

// CartRemoteDataSourceImpl.kt
package com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote

import com.example.e_commerce_app.ui.presentation.network.api.ApiServices
import com.example.e_commerce_app.ui.presentation.network.models.response.cart.CartResponseDto
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import com.example.e_commerce_app.ui.presentation.network.utilty.safeApiCall
import javax.inject.Inject

class CartRemoteDataSourceImpl @Inject constructor(
    private val apiServices: ApiServices
) : CartRemoteDataSource {
    override suspend fun getCart(): ApiResult<CartResponseDto> {
        return safeApiCall {
            apiServices.getCart()
        }
    }
}
```

### C. Repository Implementation in `ui/presentation/[feature]/data/repository/`
```kotlin
package com.example.e_commerce_app.ui.presentation.cart.data.repository

import com.example.e_commerce_app.ui.core.errors.AppErrors
import com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote.CartRemoteDataSource
import com.example.e_commerce_app.ui.presentation.cart.data.mappers.toCart
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.repository.CartRepository
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val remoteDataSource: CartRemoteDataSource
) : CartRepository {

    override suspend fun getCart(): ApiResult<Cart> {
        return when (val result = remoteDataSource.getCart()) {
            is ApiResult.ErrorApiResult -> ApiResult.ErrorApiResult(
                error = AppErrors.ServerError(message = result.error.errorMessage)
            )
            is ApiResult.SuccessApiResult -> ApiResult.SuccessApiResult(
                data = result.data.toCart()
            )
        }
    }
}
```

---

## 4. Centralized Dependency Injection (DI)

### Add bindings in `ui/core/di/CommerceModule.kt`
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class CommerceModule {

    // ... existing bindings

    @Binds
    abstract fun bindCartRemoteDataSource(
        impl: CartRemoteDataSourceImpl
    ): CartRemoteDataSource

    @Binds
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository
}
```

---

## 5. Presentation Layer (BaseViewModel MVI Architecture)

### A. MVI Contract in `ui/presentation/[feature]/feature/[subfeature]/view_model/`
> **Rule Reminder:** All contracts must implement `UiStates`, `UiIntent`, and `UiEffects` from `com.example.e_commerce_app.ui.core.mvi`.

```kotlin
// CartStates.kt
package com.example.e_commerce_app.ui.presentation.cart.feature.view_model

import com.example.e_commerce_app.ui.core.mvi.UiStates
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource

data class CartStates(
    val cartState: Resource<Cart> = Resource.Initial
) : UiStates

// CartIntent.kt
package com.example.e_commerce_app.ui.presentation.cart.feature.view_model

import com.example.e_commerce_app.ui.core.mvi.UiIntent

sealed interface CartIntent : UiIntent {
    data object LoadCart : CartIntent
    data class RemoveItem(val itemId: String) : CartIntent
}

// CartEffects.kt
package com.example.e_commerce_app.ui.presentation.cart.feature.view_model

import com.example.e_commerce_app.ui.core.mvi.UiEffects

sealed interface CartEffects : UiEffects {
    data class ShowToast(val message: String) : CartEffects
    data class NavigateToProductDetails(val productId: String) : CartEffects
}
```

### B. ViewModel in `ui/presentation/[feature]/feature/[subfeature]/view_model/CartViewModel.kt`
> **Rule Reminder:** Every ViewModel must extend `BaseViewModel<States, Intent, Effects>(initialState)`.
> - State reads: `currentState`
> - State mutations: `updateState { copy(...) }`
> - One-time events: `sendEffects { ... }`
> - Intent handling: exhaustive `when (intent)` inside `override fun processIntent(intent: CartIntent)`

```kotlin
package com.example.e_commerce_app.ui.presentation.cart.feature.view_model

import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.ui.core.mvi.BaseViewModel
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.domain.use_cases.GetCartUseCase
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.network.utilty.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase
) : BaseViewModel<CartStates, CartIntent, CartEffects>(CartStates()) {

    init {
        processIntent(CartIntent.LoadCart)
    }

    override fun processIntent(intent: CartIntent) {
        when (intent) {
            CartIntent.LoadCart -> loadCart()
            is CartIntent.RemoveItem -> removeItem(intent.itemId)
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
            updateState { copy(cartState = Resource.Loading) }
            when (val result = getCartUseCase()) {
                is ApiResult.ErrorApiResult -> {
                    updateState {
                        copy(cartState = Resource.Error(result.error.errorMessage))
                    }
                    sendEffects { CartEffects.ShowToast(result.error.errorMessage) }
                }
                is ApiResult.SuccessApiResult<Cart> -> {
                    updateState {
                        copy(cartState = Resource.Success(result.data))
                    }
                }
            }
        }
    }

    private fun removeItem(itemId: String) {
        // Read current state synchronously via currentState when needed
        val currentCart = currentState.cartState
        // Handle removal logic
    }
}
```

### C. Side-Effect Collection Helper (Compose Extension)
Place in `ui/core/mvi/MviExtensions.kt` or `ui/core/utils/`:
```kotlin
package com.example.e_commerce_app.ui.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<T>.CollectEffect(onEffect: (T) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        collect { onEffect(it) }
    }
}
```

### D. Compose Screen in `ui/presentation/[feature]/feature/[subfeature]/CartScreen.kt`
> **Rule Reminder:** Outer Stateful Composable + Inner Stateless Content Composable. Zero `NavController` inside!
> - State collected via `viewModel.states.collectAsState()`
> - Side-effects collected via `viewModel.effects.CollectEffect { ... }`
> - Intent triggering via `viewModel.processIntent(...)`

```kotlin
package com.example.e_commerce_app.ui.presentation.cart.feature

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.ui.core.components.CustomButton
import com.example.e_commerce_app.ui.core.mvi.CollectEffect
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.Cart
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartEffects
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartIntent
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartStates
import com.example.e_commerce_app.ui.presentation.cart.feature.view_model.CartViewModel
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.common.MainEmptyListSection
import com.example.e_commerce_app.ui.presentation.common.MainErrorSection
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection

// 1. Outer Stateful Composable
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onNavigateToProductDetails: (String) -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.states.collectAsState()
    val context = LocalContext.current

    viewModel.effects.CollectEffect { effect ->
        when (effect) {
            is CartEffects.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
            is CartEffects.NavigateToProductDetails -> {
                onNavigateToProductDetails(effect.productId)
            }
        }
    }

    CartContent(
        modifier = modifier,
        state = state,
        onIntent = viewModel::processIntent
    )
}

// 2. Inner Stateless Content Composable
@Composable
fun CartContent(
    modifier: Modifier = Modifier,
    state: CartStates,
    onIntent: (CartIntent) -> Unit
) {
    when (val cartResource = state.cartState) {
        is Resource.Initial,
        is Resource.Loading -> {
            MainLoadingSection(modifier = modifier)
        }
        is Resource.Error -> {
            MainErrorSection(
                modifier = modifier,
                errorMessage = cartResource.errorMessage,
                onTryAgain = { onIntent(CartIntent.LoadCart) }
            )
        }
        is Resource.Success<Cart> -> {
            val cart = cartResource.data
            if (cart.items.isEmpty()) {
                MainEmptyListSection(modifier = modifier)
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(cart.items) { item ->
                        Text(text = "${item.productTitle} - ${item.price} EGP")
                    }
                    item {
                        CustomButton(
                            text = "Checkout (${cart.totalCartPrice} EGP)",
                            onClick = { /* checkout */ }
                        )
                    }
                }
            }
        }
    }
}
```

---

## 6. Centralized Navigation Registration

### A. Define Route in `ui/core/navigation/AppRoutes.kt`
```kotlin
@Serializable
object CartRoute
```

### B. Register Destination in `ui/core/navigation/AppNavigation.kt`
```kotlin
composable<CartRoute> {
    CartScreen(
        onNavigateToProductDetails = { productId ->
            navController.navigate(ProductDetailsRoute(productId))
        }
    )
}
```

---

## 7. Step-by-Step Integration Checklist

Use this checklist when building any feature to ensure zero architectural regressions:

- [ ] **1. Network Constants & Endpoint:** Add path constant to `ApiEndPoints.kt` and suspension function to `ApiServices.kt`.
- [ ] **2. DTOs Creation:** Place requests in `network/models/request/` and responses in `network/models/response/`. Check: Are ID fields cleanly separated from nested models?
- [ ] **3. Domain Models:** Create pure Kotlin entities in `[feature]/domain/entities/` (zero Android/Retrofit/Gson dependencies).
- [ ] **4. Mapper Creation:** Write extension functions `Dto.to[Model]()` in `[feature]/data/mappers/`.
- [ ] **5. Domain Repository & UseCase:** Create repository interface in `[feature]/domain/repository/` returning `ApiResult<T>`, and single-action UseCases with `suspend operator fun invoke()`.
- [ ] **6. Remote DataSource:** Write interface and implementation in `[feature]/data/data_sources/remote/` using `safeApiCall { ... }`.
- [ ] **7. Repository Implementation:** Implement repository in `[feature]/data/repository/`, mapping DTOs to Domain models.
- [ ] **8. Centralized DI Registration:** Add `@Binds` for DataSource and Repository into `ui/core/di/CommerceModule.kt`.
- [ ] **9. MVI Contract Definition:** Define `<Feature>States : UiStates` (data class), `<Feature>Intent : UiIntent` (sealed interface), `<Feature>Effects : UiEffects` (sealed interface).
- [ ] **10. ViewModel Implementation:** Extend `BaseViewModel<<Feature>States, <Feature>Intent, <Feature>Effects>(initialState)`. Read state via `currentState`, mutate via `updateState { copy(...) }`, emit one-time effects via `sendEffects { ... }`, and handle all actions inside `override fun processIntent(intent: <Feature>Intent)`.
- [ ] **11. Compose UI Implementation:**
  - Create Outer Stateful Composable with `hiltViewModel()`, collecting state via `viewModel.states.collectAsState()` and one-off effects via `viewModel.effects.CollectEffect { ... }`.
  - Dispatch actions via `onIntent = viewModel::processIntent`.
  - Create Inner Stateless Content Composable with `(state, onIntent)`.
  - Reuse `MainLoadingSection`, `MainErrorSection`, and `MainEmptyListSection`.
  - Reuse `CustomButton` and `CustomTextField`.
- [ ] **12. Centralized Navigation Wiring:** Define route object in `AppRoutes.kt` and plug destination into `AppNavigation.kt` with lambda navigation handlers (no `NavController` passed down).

