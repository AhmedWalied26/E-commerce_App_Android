# Feature Specification: Wishlist (`@commerce`, `@network`, `@di`, `@core`)

## 1. Overview & User Flows

The Wishlist feature provides users with the ability to bookmark their favorite products, manage their curated wishlist, seamlessly transfer items to the shopping cart, and toggle favorite states directly from product browsing surfaces.

### Key User Flows:

1. **Browsing the Wishlist:**
   - User navigates to the Wishlist tab (index `2` in `MainScreen` bottom navigation bar).
   - `WishListViewModel` initiates `GET api/v1/wishlist` via `WishListIntent.LoadWishlist` **only when the tab is accessed**.
   - No global wishlist fetch is executed during app startup (`MainScreenViewModel.init`), and no wishlist data or IDs are persisted locally (no DataStore, SharedPreferences, or database).
   - Wishlist tab icon displays **no** item count or badge.

2. **Wishlist Screen Layout & Item Cards:**
   - Rendered with top search bar layout consistent with the application standard (`CustomAppBar`).
   - Vertically scrollable list displaying `WishlistItemCard` items.
   - Each card displays:
     - Asynchronous product cover image inside a rounded container.
     - Product title and color/category attribute presentation.
     - Current price in EGP, alongside a struck-through original price if `priceAfterDiscount` exists.
     - Filled heart button (`R.drawable.added_to_favourite_icon`) to remove the item from the wishlist.
     - "Add to Cart" button (`CustomButton` / pill button) to transfer the item to the cart.

3. **Removing an Item from Wishlist:**
   - User taps the filled heart button on a card in `WishListScreen`.
   - Dispatches `WishListIntent.RemoveFromWishlist(productId)`.
   - Calls `DELETE api/v1/wishlist/{productId}`.
   - On success:
     - The item is removed from the visible list.
     - Shows feedback toast: `"Product removed successfully from your wishlist"`.
   - If the list becomes empty, transitions to `MainEmptyListSection`.

4. **Adding an Item to Cart from Wishlist:**
   - User taps "Add to Cart" on a wishlist card.
   - Dispatches `WishListIntent.AddToCart(productId)`.
   - Calls `POST api/v1/cart` via the existing `AddToCartUseCase`.
   - On success:
     - Shows feedback toast: `"Added to cart successfully"`.
     - The item remains in the wishlist (does not automatically remove).
     - Reactive global `cartCount` updates immediately across the app.

5. **Product Card Heart Toggle in Product Catalog (`ProductTab`):**
   - Each product card in the product catalog (`ProductItem`) features a favorite heart icon.
   - **Tapping an unfilled heart:**
     - Calls `POST api/v1/wishlist` with `{ "productId": "<productId>" }`.
     - On success: updates that card's state to filled heart and shows toast: `"Product added successfully to your wishlist"`.
   - **Tapping a filled heart:**
     - Calls `DELETE api/v1/wishlist/{productId}`.
     - On success: updates that card's state to unfilled heart and shows toast: `"Product removed successfully from your wishlist"`.
   - Requests are guarded against simultaneous duplicate taps per card during loading.

6. **Error Handling & Retry:**
   - Network or server errors render `MainErrorSection` with an error message and a retry button that re-triggers `WishListIntent.LoadWishlist`.

---

## 2. MVI Contract & Screen State

Located in: `ui/presentation/wishList/view_model/`

### 1. States (`WishListContract.kt`)
```kotlin
data class WishListStates(
    val wishlistState: Resource<List<WishlistItem>> = Resource.Initial,
    val removingProductId: String? = null,
    val addingToCartProductId: String? = null
) : UiStates
```

### 2. Intent (`WishListContract.kt`)
```kotlin
sealed interface WishListIntent : UiIntent {
    data object LoadWishlist : WishListIntent
    data class RemoveFromWishlist(val productId: String) : WishListIntent
    data class AddToCart(val productId: String) : WishListIntent
}
```

### 3. Effects (`WishListContract.kt`)
```kotlin
sealed interface WishListEffects : UiEffects {
    data class ShowToast(val message: String) : WishListEffects
    data class NavigateToProductDetails(val productId: String) : WishListEffects
}
```

### 4. UI State Handling in `WishListScreen.kt`
- **`Resource.Initial` & `Resource.Loading`:** Renders `MainLoadingSection()`.
- **`Resource.Error`:** Renders `MainErrorSection(errorMessage = state.errorMessage, onTryAgain = { onIntent(WishListIntent.LoadWishlist) })`.
- **`Resource.Success<List<WishlistItem>>`:**
  - If `data.isEmpty()`: Renders `MainEmptyListSection(message = "Your wishlist is empty", description = "Explore products and add items you love!")`.
  - If `data.isNotEmpty()`: Renders `LazyColumn` of `WishlistItemCard` items.

---

## 3. API Endpoints & DTOs

### A. Endpoints & Retrofit Service (`api/v1/wishlist`)
Defined in `ui/presentation/network/api/ApiEndPoints.kt` & `ApiServices.kt`:
```kotlin
// ApiEndPoints.kt
const val WISHLIST = "wishlist"

// ApiServices.kt
@GET(ApiEndPoints.WISHLIST)
suspend fun getWishlist(
    @Header("token") token: String
): GetWishlistResponseDto

@POST(ApiEndPoints.WISHLIST)
suspend fun addToWishlist(
    @Header("token") token: String,
    @Body body: AddWishlistRequestDto
): WishlistActionResponseDto

@DELETE("${ApiEndPoints.WISHLIST}/{productId}")
suspend fun removeFromWishlist(
    @Header("token") token: String,
    @Path("productId") productId: String
): WishlistActionResponseDto
```

### B. Strict DTO Parsing Rules (Polymorphic `data` Format)
`POST` and `DELETE` return `data` as a list of raw product ID strings (`List<String>`), whereas `GET` returns `data` as a list of full nested product objects (`List<ProductItemDto>`). Separate DTOs are required to avoid `JsonSyntaxException`:

1. **Action Response DTO (`POST` & `DELETE`):**
```kotlin
data class WishlistActionResponseDto(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null,
    @field:SerializedName("data") val data: List<String>? = null
)
```

2. **Request DTO (`POST`):**
```kotlin
data class AddWishlistRequestDto(
    @field:SerializedName("productId") val productId: String
)
```

3. **Get Wishlist Response DTO (`GET`):**
```kotlin
data class GetWishlistResponseDto(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("count") val count: Int? = null,
    @field:SerializedName("data") val data: List<ProductItemDto>? = null
)
```

### C. Domain Entities
Located in `ui/presentation/wishList/domain/entities/`:
```kotlin
data class WishlistItem(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val priceAfterDiscount: Int?,
    val imageCover: String,
    val ratingsAverage: Double,
    val categoryName: String?,
    val brandName: String?,
    val colorPresentation: String?
)
```

### D. Mappers
Located in `ui/presentation/wishList/data/mappers/WishlistMapper.kt`:
- `ProductItemDto.toWishlistItem(): WishlistItem`

### E. Data Sources & Repository Flow
- **`WishlistRemoteDataSource` / `WishlistRemoteDataSourceImpl`:**
  Injects `ApiServices` and `AuthLocalDataSource` (to retrieve current auth token) and makes authenticated network calls wrapped in `safeApiCall`.
- **`WishlistRepository` / `WishlistRepositoryImpl`:**
  - `getWishlist(): ApiResult<List<WishlistItem>>`
  - `addToWishlist(productId: String): ApiResult<Unit>`
  - `removeFromWishlist(productId: String): ApiResult<Unit>`
- **Use Cases:**
  - `GetWishlistUseCase`: returns `ApiResult<List<WishlistItem>>`
  - `AddToWishlistUseCase`: returns `ApiResult<Unit>`
  - `RemoveFromWishlistUseCase`: returns `ApiResult<Unit>`
- **DI Registration:** Centralized in `ui/core/di/CommerceModule.kt`.

---

## 4. Navigation & Entry Points

### Route Definition in `AppRoutes.kt`
```kotlin
@Serializable
object WishlistRoute
```

### NavHost Wiring in `AppNavigation.kt`
```kotlin
composable<WishlistRoute> {
    WishListScreen()
}
```

### Bottom Navigation Tab in `MainScreen.kt`
- Selection of tab index `2` renders `WishListTab()` / `WishListScreen()`.
- Bottom navigation item does not display any badge counter.
- Wishlist data is fetched when the tab is displayed.

