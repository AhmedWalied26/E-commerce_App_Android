# Feature Specification: Products (`@commerce`, `@network`, `@di`)

## 1. Overview & User Flows

The Products feature provides a comprehensive product catalog browsing experience, enabling users to explore items, view pricing and discounts, inspect ratings, and prepare items for cart or wishlist actions.

### Key User Flows:
1. **Product Feed Browsing:**
   - Navigating to the Products tab (`ProductTab`, index `1` in `MainScreen` bottom navigation) automatically triggers `ProductIntent.LoadProducts`.
   - Products are rendered in a 2-column adaptive vertical grid (`LazyVerticalGrid(columns = GridCells.Fixed(2))`).
   - Each product card (`ProductItem`) displays:
     - Asynchronous cover image (`SubcomposeAsyncImage`) with rounded top corners.
     - Favorite / Wishlist toggle button floating on top-right of the image.
     - Product title and description truncated with ellipsis (`maxLines = 1`).
     - Current price in EGP, and crossed-out original price if a discount is present.
     - Rating display (star icon, rating score, and rating reviews count).
     - Circular Add button on the bottom right for cart addition.
2. **Success & Side Effects:**
   - On successful load, `ProductEffects.ShowToast("Loaded Successfully")` is dispatched to inform the user.
3. **Error Handling & Retry:**
   - If the network or server fails, `MainErrorSection` is rendered with the error message.
   - User can tap "Try again" to re-request product data.

---

## 2. MVI Contract & Screen State

Located in: `ui/presentation/commerce/feature/main/tabs/product/view_model/`

### 1. States (`ProductStates.kt`)
```kotlin
data class ProductStates(
    val productState: Resource<List<ProductItem>> = Resource.Initial
)
```

### 2. Intent (`ProductViewModel.kt`)
```kotlin
sealed interface ProductIntent {
    data object LoadProducts : ProductIntent
}
```

### 3. Effects (`ProductViewModel.kt`)
```kotlin
sealed interface ProductEffects {
    data class ShowToast(val message: String) : ProductEffects
}
```

### 4. UI State Handling in `ProductTab.kt`
- **`Resource.Initial` & `Resource.Loading`:**
  Renders `MainLoadingSection()`.
- **`Resource.Success<List<ProductItem>>`:**
  Renders `LazyVerticalGrid(columns = GridCells.Fixed(2))` with `16.dp` spacing:
  ```kotlin
  items(productState.data) { product ->
      ProductItem(product = product)
  }
  ```
- **`Resource.Error`:**
  Renders `MainErrorSection(errorMessage = productState.errorMessage, onTryAgain = { ... })`.

---

## 3. API Endpoints & DTOs

### A. Endpoint & Retrofit Service
Defined in `ui/presentation/network/api/ApiEndPoints.kt` & `ApiServices.kt`:
```kotlin
// ApiEndPoints.kt
const val PRODUCTS = "products"

// ApiServices.kt
@GET(ApiEndPoints.PRODUCTS)
suspend fun getAllProducts(): ProductResponseDto
```

### B. Network DTOs
Located in `ui/presentation/network/models/response/products/`:
```kotlin
// ProductResponseDto.kt
data class ProductResponseDto(
    @field:SerializedName("metadata") val metadata: MetadataDto? = null,
    @field:SerializedName("data") val data: List<ProductItemDto>? = null,
    @field:SerializedName("results") val results: Int? = null
)

// ProductItemDto.kt
data class ProductItemDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("title") val title: String? = null,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("quantity") val quantity: Int? = null,
    @field:SerializedName("price") val price: Int? = null,
    @field:SerializedName("priceAfterDiscount") val priceAfterDiscount: Int? = null,
    @field:SerializedName("imageCover") val imageCover: String? = null,
    @field:SerializedName("images") val images: List<String>? = null,
    @field:SerializedName("sold") val sold: Int? = null,
    @field:SerializedName("ratingsAverage") val ratingsAverage: Any? = null,
    @field:SerializedName("ratingsQuantity") val ratingsQuantity: Int? = null,
    @field:SerializedName("category") val category: CategoryItemDto? = null,
    @field:SerializedName("subcategory") val subcategory: List<CategoryItemDto>? = null,
    @field:SerializedName("brand") val brand: CategoryItemDto? = null,
    @field:SerializedName("slug") val slug: String? = null,
    @field:SerializedName("availableColors") val availableColors: List<Any?>? = null,
    @field:SerializedName("createdAt") val createdAt: String? = null,
    @field:SerializedName("updatedAt") val updatedAt: String? = null
)
```

### C. Domain Entity
Located in `ui/presentation/commerce/domain/entities/products/ProductItem.kt`:
```kotlin
data class ProductItem(
    val images: List<String?>? = null,
    val quantity: Int? = null,
    val imageCover: String? = null,
    val description: String? = null,
    val title: String? = null,
    val ratingsQuantity: Int? = null,
    val ratingsAverage: Any? = null,
    val price: Int? = null,
    val id: String? = null,
    val subcategory: List<CategoryItemDto?>? = null,
    val category: CategoryItemDto? = null,
    val brand: CategoryItemDto? = null,
    val priceAfterDiscount: Int? = null
)
```

### D. Mapper
Located in `ui/presentation/commerce/data/mappers/ProductMapper.kt`:
```kotlin
fun ProductItemDto.toProductItem(): ProductItem {
    return ProductItem(
        images = images,
        quantity = quantity,
        imageCover = imageCover,
        description = description,
        title = title,
        ratingsQuantity = ratingsQuantity,
        ratingsAverage = ratingsAverage,
        price = price,
        id = id,
        subcategory = subcategory,
        category = category,
        brand = brand,
        priceAfterDiscount = priceAfterDiscount
    )
}
```

### E. Data Source & Repository Flow
1. **Remote DataSource:** `HomeRemoteDataSource.getProducts(): ApiResult<ProductResponseDto>`
   - Implementation: `HomeRemoteDataSourceImpl` invokes `safeApiCall { apiServices.getAllProducts() }`.
2. **Repository:** `HomeRepository.getProducts(): ApiResult<List<ProductItem>>`
   - Implementation: `HomeRepositoryImpl` transforms `ProductResponseDto.data` via `productItemDto.toProductItem()`.
3. **UseCase:** `GetProductsUseCase @Inject constructor(val homeRepository: HomeRepository)`
   - Implementation: `suspend fun invoke(): ApiResult<List<ProductItem>> = homeRepository.getProducts()`.
4. **DI Binding:** Centralized in `ui/core/di/CommerceModule.kt`.

---

## 4. Navigation & Entry Points

- **Primary Route:** Hosted in `MainRoute` (`ui/core/navigation/AppRoutes.kt`).
- **Destination:** Rendered in `MainScreen` on tab index `1` (`ProductTab`) when selected via `CustomBottomBar`:
  ```kotlin
  when (selectedIndex) {
      0 -> HomeTab()
      1 -> ProductTab()
      2 -> WishListTab()
      3 -> ProfileTab()
  }
  ```
- **Graph Connection:** Plugged into `AppNavigation.kt`:
  ```kotlin
  composable<MainRoute> {
      MainScreen(
          logout = {
              navController.navigate(LoginRoute) {
                  popUpTo(0) { inclusive = true }
              }
          }
      )
  }
  ```
- Screen components never hold direct references to `NavController`.

