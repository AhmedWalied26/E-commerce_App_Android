# Feature Specification: Cart (`@commerce`, `@network`, `@di`, `@core`)

## 1. Overview & User Flows

The Cart feature provides a centralized shopping bag experience, allowing users to view added items, modify product quantities, delete individual items, monitor the live cart count across the application, and proceed to checkout.

### Key User Flows:
1. **Live Global Cart Counter & Badge:**
   - On application startup or when entering `MainScreen`, `MainScreenViewModel` triggers `getCart()` to initialize the reactive badge counter (`cartCount: StateFlow<Int>`).
   - The cart icon in `CustomAppBar` displays a Material 3 `BadgedBox` with a `Badge` showing the count whenever `cartCount > 0`.
   - **Badge Update Rules:**
     - **Add (`POST api/v1/cart`):** Successfully adding an item updates `cartCount` to the new `numOfCartItems`.
     - **Remove (`DELETE api/v1/cart/{productId}`):** Successfully removing an item updates `cartCount`.
     - **Initial Fetch (`GET api/v1/cart`):** Initializes `cartCount`.
     - **Quantity Update (`PUT api/v1/cart/{productId}`):** Does **NOT** alter `cartCount`, as `numOfCartItems` tracks distinct products, not total unit quantities.
2. **Adding to Cart from Product Catalog:**
   - In `ProductTab`, each product card features an "Add to Cart" button (`R.drawable.add_to_cart_filled`).
   - Clicking it triggers `AddToCartUseCase(productId)`.
   - On success, the global cart badge updates immediately, and a confirmation toast/snackbar is shown.
3. **Cart Screen Browsing (`CartScreen`):**
   - User taps the cart icon in `CustomAppBar` to navigate to `CartRoute`.
   - The screen displays a scrollable list of cart items along with product thumbnail, title, color/size attributes, item price, a delete button, and a quantity selector.
4. **Quantity Adjustment:**
   - User taps the `+` (`increment_icon`) or `-` (`decrement_icon`) buttons on an item card.
   - Dispatches `CartIntent.UpdateProductQuantity(productId, newCount)`.
   - If count is reduced to `0`, it triggers removal of the product from the cart.
5. **Item Removal:**
   - User taps the trash icon (`delete_icon`) on a cart item.
   - Dispatches `CartIntent.RemoveProductFromCart(productId)`.
   - Item is removed, total price updates, and `cartCount` decrements.
6. **Empty Cart State:**
   - If the cart contains 0 items, `MainEmptyListSection` is displayed with a descriptive message and an invitation to browse products.
7. **Checkout Navigation:**
   - The sticky bottom bar displays "Total price" (in EGP) and a prominent "Check Out ->" button.
   - Tapping it dispatches `CartIntent.OnCheckOutClicked` and triggers `CartEffects.NavigateToCheckout`.

---

## 2. MVI Contract & Screen State

Located in: `ui/presentation/cart/feature/view_model/`

### 1. States (`CartStates.kt`)
```kotlin
data class CartStates(
    val cartState: Resource<Cart> = Resource.Initial,
    val isUpdatingItem: String? = null // Holds the productId currently being updated/removed
)
```

### 2. Intent (`CartViewModel.kt`)
```kotlin
sealed interface CartIntent {
    data object LoadCart : CartIntent
    data class UpdateProductQuantity(val productId: String, val count: Int) : CartIntent
    data class RemoveProductFromCart(val productId: String) : CartIntent
    data object ClearCart : CartIntent
    data object OnCheckOutClicked : CartIntent
    data object OnBackClicked : CartIntent
}
```

### 3. Effects (`CartViewModel.kt`)
```kotlin
sealed interface CartEffects {
    data class ShowToast(val message: String) : CartEffects
    data class NavigateToProductDetails(val productId: String) : CartEffects
    data object NavigateToCheckout : CartEffects
    data object NavigateBack : CartEffects
}
```

### 4. UI State Handling in `CartScreen.kt`
- **`Resource.Initial` & `Resource.Loading`:** Renders `MainLoadingSection()`.
- **`Resource.Error`:** Renders `MainErrorSection(errorMessage, onTryAgain = { onIntent(CartIntent.LoadCart) })`.
- **`Resource.Success<Cart>`:**
  - If `cart.products.isEmpty()`: Renders `MainEmptyListSection(message = "Your cart is empty")`.
  - If `cart.products.isNotEmpty()`:
    - Top App Bar with back navigation, "Cart" title, search and cart icons.
    - LazyColumn rendering `CartItemCard` for each product.
    - Bottom bar displaying "Total price" and "Check Out ->" button (`CustomButton`).

---

## 3. API Endpoints & DTOs

### A. Endpoints & Retrofit Service (`api/v1/cart`)
Defined in `ui/presentation/network/api/ApiEndPoints.kt` & `ApiServices.kt`:
```kotlin
// ApiEndPoints.kt
const val CART = "cart"

// ApiServices.kt
@GET(ApiEndPoints.CART)
suspend fun getCart(
    @Header("token") token: String
): CartResponseDto

@POST(ApiEndPoints.CART)
suspend fun addToCart(
    @Header("token") token: String,
    @Body request: AddToCartRequestDto
): AddToCartResponseDto

@PUT("${ApiEndPoints.CART}/{productId}")
suspend fun updateCartQuantity(
    @Header("token") token: String,
    @Path("productId") productId: String,
    @Body request: UpdateCartQuantityRequestDto
): CartResponseDto

@DELETE("${ApiEndPoints.CART}/{productId}")
suspend fun removeCartItem(
    @Header("token") token: String,
    @Path("productId") productId: String
): CartResponseDto

@DELETE(ApiEndPoints.CART)
suspend fun clearCart(
    @Header("token") token: String
): CartResponseDto
```

### B. Strict DTO Parsing Rule (Polymorphic `product` Field)
Because the backend returns `product` as a full JSON object in `GET`/`PUT`/`DELETE` and as a raw `String` ID in `POST`, separate DTOs must be maintained to prevent `JsonSyntaxException`:

1. **`CartResponseDto` (for `GET`, `PUT`, `DELETE`):**
```kotlin
data class CartResponseDto(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("numOfCartItems") val numOfCartItems: Int? = null,
    @field:SerializedName("cartId") val cartId: String? = null,
    @field:SerializedName("data") val data: CartDataDto? = null
)

data class CartDataDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("cartOwner") val cartOwner: String? = null,
    @field:SerializedName("products") val products: List<CartProductItemDto>? = null,
    @field:SerializedName("totalCartPrice") val totalCartPrice: Int? = null
)

data class CartProductItemDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("count") val count: Int? = null,
    @field:SerializedName("price") val price: Int? = null,
    @field:SerializedName("product") val product: CartProductDto? = null
)

data class CartProductDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("title") val title: String? = null,
    @field:SerializedName("imageCover") val imageCover: String? = null,
    @field:SerializedName("quantity") val quantity: Int? = null,
    @field:SerializedName("ratingsAverage") val ratingsAverage: Double? = null,
    @field:SerializedName("category") val category: CategoryItemDto? = null,
    @field:SerializedName("brand") val brand: CategoryItemDto? = null
)
```

2. **`AddToCartResponseDto` (for `POST`):**
```kotlin
data class AddToCartResponseDto(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null,
    @field:SerializedName("numOfCartItems") val numOfCartItems: Int? = null,
    @field:SerializedName("cartId") val cartId: String? = null,
    @field:SerializedName("data") val data: AddToCartDataDto? = null
)

data class AddToCartDataDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("cartOwner") val cartOwner: String? = null,
    @field:SerializedName("products") val products: List<AddToCartProductItemDto>? = null,
    @field:SerializedName("totalCartPrice") val totalCartPrice: Int? = null
)

data class AddToCartProductItemDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("count") val count: Int? = null,
    @field:SerializedName("price") val price: Int? = null,
    @field:SerializedName("product") val product: String? = null // Raw String ID!
)
```

3. **Request DTOs:**
```kotlin
data class AddToCartRequestDto(
    @field:SerializedName("productId") val productId: String
)

data class UpdateCartQuantityRequestDto(
    @field:SerializedName("count") val count: String
)
```

### C. Domain Entities
Located in `ui/presentation/cart/domain/entities/`:
```kotlin
data class Cart(
    val id: String,
    val totalCartPrice: Int,
    val numOfCartItems: Int,
    val products: List<CartProductItem>
)

data class CartProductItem(
    val id: String,
    val count: Int,
    val price: Int,
    val product: CartProduct
)

data class CartProduct(
    val id: String,
    val title: String,
    val imageCover: String,
    val ratingsAverage: Double,
    val categoryName: String,
    val brandName: String
)
```

### D. Mappers
Located in `ui/presentation/cart/data/mappers/CartMapper.kt`:
- `CartResponseDto.toCart(): Cart`
- `CartProductItemDto.toCartProductItem(): CartProductItem`
- `CartProductDto.toCartProduct(): CartProduct`

### E. Data Sources & Repository Flow
- **`CartRemoteDataSource` / `CartRemoteDataSourceImpl`:**
  Injects `ApiServices` and `AuthLocalDataSource` (to retrieve current auth token) and makes authenticated network calls wrapped in `safeApiCall`.
- **`CartRepository` / `CartRepositoryImpl`:**
  - Maintains a shared `_cartCount = MutableStateFlow(0)` exposed as `val cartCount: StateFlow<Int>`.
  - In `getCart()`, updates `_cartCount.value = result.data.numOfCartItems ?: 0`.
  - In `addToCart()`, updates `_cartCount.value = result.data.numOfCartItems ?: 0`.
  - In `removeCartItem()`, updates `_cartCount.value = result.data.numOfCartItems ?: 0`.
  - In `updateProductQuantity()`, updates cart data but leaves `_cartCount` untouched.
  - In `clearCart()`, updates `_cartCount.value = 0`.
- **Use Cases:**
  - `GetCartUseCase`: returns `ApiResult<Cart>`
  - `AddToCartUseCase`: returns `ApiResult<Unit>`
  - `UpdateCartQuantityUseCase`: returns `ApiResult<Cart>`
  - `RemoveCartItemUseCase`: returns `ApiResult<Cart>`
  - `ClearCartUseCase`: returns `ApiResult<Cart>`
  - `GetCartCountUseCase`: returns `StateFlow<Int>`
- **DI Registration:** Bound in `ui/core/di/CommerceModule.kt`.

---

## 4. Navigation & Entry Points

### Route Definition in `AppRoutes.kt`
```kotlin
@Serializable
object CartRoute
```

### NavHost Wiring in `AppNavigation.kt`
```kotlin
composable<CartRoute> {
    CartScreen(
        onBackClick = {
            navController.popBackStack()
        },
        onNavigateToProductDetails = { productId ->
            // For future details integration
        }
    )
}
```

### Screen Entry Points
- `CustomAppBar.kt` (in `MainScreen`):
  Clicking the badged cart icon calls `onNavigateToCart: () -> Unit`, navigating directly to `CartRoute`.

