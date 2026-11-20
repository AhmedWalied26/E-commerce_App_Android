# Feature Specification: Product Details (`@commerce`, `@network`, `@cart`, `@wishlist`)

## 1. Overview & User Flows

The Product Details feature provides a detailed view of a selected product, allowing users to browse high-resolution images via a horizontal carousel, inspect pricing, ratings, sold count, and descriptions, choose temporary customization options (size and color), select purchase quantity, toggle wishlist status, and add items to the cart.

### Key User Flows:

1. **Screen Entry & Data Loading:**
   - User enters Product Details **only** by tapping a product from:
     - A product card in the **Product Tab** (`ProductItem`).
     - A product card in the **Wishlist Tab** (`WishlistItemCard`).
     - A product item in the **Cart Screen** (`CartItemCard`).
   - The originating screen passes `productId` as a type-safe navigation argument via `ProductDetailsRoute(productId)`.
   - **In-Memory Resolution (No New Network Requests):** On screen launch, `ProductDetailsIntent.LoadProductDetails(productId)` resolves the product directly from the products already fetched and cached in memory by the Product Tab (`HomeRepository.getProductById(productId)`). No network request (`GET /products/{id}`) is made.
   - The screen checks initial wishlist status (syncing with `GetWishlistUseCase`) and cart counter (`GetCartCountUseCase`).

2. **Image Carousel:**
   - Displays a horizontal pager with the product's `images` list (fallback to `imageCover`).
   - Shows rounded indicator dots at the bottom with the active page highlighted as an elongated dark-blue pill.
   - Includes fallback placeholder if images fail to load.

3. **Wishlist Heart Toggle:**
   - Heart button in upper-right corner over the image.
   - If wishlisted: displays filled heart (`R.drawable.added_to_favourite_icon`).
   - If not wishlisted: displays outline heart (`R.drawable.selected_favourite_tab_icon`).
   - Tapping invokes existing `AddToWishlistUseCase` or `RemoveFromWishlistUseCase`.
   - Visual state toggles only upon API success; feedback shown via toast/snackbar.

4. **Product Info & Description Expansion:**
   - Displays title, price in EGP, sold count (`{sold} Sold` pill), and rating (`★ {ratingsAverage} ({ratingsQuantity})`).
   - Displays description with initial collapsed limit (e.g., 3 lines) and a "Read More" / "Read Less" toggle button.

5. **Size & Color Selectors:**
   - **Size Selector:** Displays available sizes (e.g. `38`, `39`, `40`, `41`, `42`). Selected size is rendered in a dark-blue filled circle; unselected items are normal text chips.
   - **Color Selector:** Displays circular color swatches. Selected swatch shows a white checkmark icon.
   - Size and color selections are maintained strictly as temporary screen UI state and are not persisted.

6. **Quantity Selector:**
   - Minus (`-`), count, and Plus (`+`) buttons in a rounded blue container.
   - Initial quantity is `1`.
   - Minimum is `1` (cannot decrease below 1; minus button disabled when quantity is 1).
   - Maximum is constrained by the product's available `quantity` (stock); plus button disabled when stock limit is reached.
   - Quantity is temporary screen UI state.

7. **Add to Cart & Live Badge Flow:**
   - Bottom bar displays total price dynamically (`selectedQuantity * price`).
   - Tapping "Add to cart":
     - Calls `AddToCartUseCase(productId)`.
     - If `selectedQuantity > 1`: sequentially invokes `UpdateCartQuantityUseCase` to set the exact selected quantity in cart.
     - Live cart counter updates reactively via existing `CartRepository.cartCount`.
     - Dispatches success effect ("Product added to cart successfully").

8. **Navigation Actions:**
   - Back arrow: navigates back to the originating screen via `navController.popBackStack()`.
   - Search icon: reserved for search navigation/action.
   - Cart icon: navigates to `CartRoute` with live cart badge.

9. **Error Handling & Out of Stock:**
   - If Product Tab data is unavailable or the product ID cannot be found in memory, the screen displays `MainErrorSection` with a "Back" action to return to the previous screen. No fallback network request is triggered.
   - If product `quantity <= 0`, displays "Out of Stock" indicator and disables "Add to cart".

---

## 2. MVI Contract & Screen State

Located in: `ui/presentation/commerce/feature/product_details/view_model/`

### 1. States (`ProductDetailsStates.kt`)
```kotlin
data class ProductDetailsStates(
    val productDetailsState: Resource<ProductItem> = Resource.Initial,
    val selectedQuantity: Int = 1,
    val selectedSize: String = "40",
    val selectedColorIndex: Int = 1,
    val isFavorite: Boolean = false,
    val isFavoriteLoading: Boolean = false,
    val isAddingToCart: Boolean = false,
    val cartCount: Int = 0,
    val isDescriptionExpanded: Boolean = false
) : UiStates
```

### 2. Intent (`ProductDetailsIntent.kt`)
```kotlin
sealed interface ProductDetailsIntent : UiIntent {
    data class LoadProductDetails(val productId: String) : ProductDetailsIntent
    data object IncrementQuantity : ProductDetailsIntent
    data object DecrementQuantity : ProductDetailsIntent
    data class SelectSize(val size: String) : ProductDetailsIntent
    data class SelectColor(val colorIndex: Int) : ProductDetailsIntent
    data object ToggleDescriptionExpanded : ProductDetailsIntent
    data object ToggleWishlist : ProductDetailsIntent
    data object AddToCart : ProductDetailsIntent
}
```

### 3. Effects (`ProductDetailsEffects.kt`)
```kotlin
sealed interface ProductDetailsEffects : UiEffects {
    data class ShowToast(val message: String) : ProductDetailsEffects
    data object NavigateBack : ProductDetailsEffects
    data object NavigateToCart : ProductDetailsEffects
}
```

---

## 3. API Endpoints & DTOs

### A. Endpoint & Retrofit Service
In `ui/presentation/network/api/ApiEndPoints.kt` & `ApiServices.kt`:
```kotlin
// ApiEndPoints.kt
const val PRODUCTS = "products"

// ApiServices.kt
@GET("${ApiEndPoints.PRODUCTS}/{id}")
suspend fun getProductDetails(@Path("id") id: String): ProductDetailsResponseDto
```

### B. Network DTO
Located in `ui/presentation/network/models/response/products/ProductDetailsResponseDto.kt`:
```kotlin
data class ProductDetailsResponseDto(
    @field:SerializedName("data")
    val data: ProductItemDto? = null
)
```

### C. Domain Entity
Located in `ui/presentation/commerce/domain/entities/products/ProductItem.kt`:
- Reuses existing `ProductItem` with `sold: Int? = null` added.

### D. Mapper
Located in `ui/presentation/commerce/data/mappers/ProductMapper.kt`:
- Maps `sold = sold` from `ProductItemDto` to `ProductItem`.

### E. Data Source & Repository Flow
1. **DataSource:** `HomeRemoteDataSource.getProductDetails(productId: String): ApiResult<ProductDetailsResponseDto>`
2. **Repository:** `HomeRepository.getProductDetails(productId: String): ApiResult<ProductItem>`
3. **UseCase:** `GetProductDetailsUseCase(homeRepository: HomeRepository)`
4. **DI Binding:** Centralized in `ui/core/di/CommerceModule.kt`.

---

## 4. Navigation & Entry Points

### A. Route Definition (`AppRoutes.kt`)
```kotlin
@Serializable
data class ProductDetailsRoute(val productId: String)
```

### B. Navigation Graph (`AppNavigation.kt`)
```kotlin
composable<ProductDetailsRoute> {
    ProductDetailsScreen(
        onBackClick = { navController.popBackStack() },
        onNavigateToCart = { navController.navigate(CartRoute) }
    )
}
```

### C. Originating Screen Triggers
1. **`ProductItem.kt` (Product Tab):** Card tap invokes `onProductClick(productId)`.
2. **`WishlistItemCard.kt` (Wishlist Tab):** Card tap invokes `onProductClick(productId)`.
3. **`CartItemCard.kt` (Cart Screen):** Product thumbnail / title tap invokes `onProductClick(productId)`.

