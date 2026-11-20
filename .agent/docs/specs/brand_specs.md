# Feature Specification: Brands (`@commerce`, `@network`, `@di`)

## 1. Overview & User Flows

The Brands feature highlights official merchant and manufacturer brands on the Home screen dashboard, allowing users to discover products by brand identity.

### Key User Flows:
1. **Home Feed Brand Showcase:**
   - On initializing `HomeTab` within `MainScreen`, `HomeIntent.LoadBrand` is dispatched simultaneously with category loading.
   - Displays a 2-row horizontally scrolling collection of official brand logos and brand names (`HomeSuccessSection`).
   - Images are loaded asynchronously via Coil with circular clipping.
2. **Independent Error Recovery:**
   - If the brand API fails while categories succeed (or vice versa), the brand section independently renders `MainErrorSection`.
   - Tapping "Try again" retries only the brand loading process without disrupting other feed sections.
3. **Brand Browsing:**
   - Provides a "view all" brand header action for full catalog navigation.

---

## 2. MVI Contract & Screen State

Brand state is coordinated within the Home dashboard MVI contract:
Located in: `ui/presentation/commerce/feature/main/tabs/home/view_model/`

### 1. States (`HomeStates.kt`)
```kotlin
data class HomeStates(
    val categoryState: Resource<List<CategoryItem>> = Resource.Initial,
    val brandState: Resource<List<CategoryItem>> = Resource.Initial
)
```

### 2. Intent (`HomeViewModel.kt`)
```kotlin
sealed interface HomeIntent {
    data object LoadCategory : HomeIntent
    data object LoadBrand : HomeIntent
}
```

### 3. UI State Handling in `HomeTab.kt`
The brand section manages state transitions independently from categories:
- **`Resource.Initial` & `Resource.Loading`:**
  Renders `MainLoadingSection()`.
- **`Resource.Success<List<CategoryItem>>`:**
  Renders `HomeSuccessSection(list = brandState.data)`.
  - Grid: `LazyHorizontalGrid(rows = GridCells.Fixed(2), modifier = Modifier.height(260.dp))`
  - Displays circular brand logos and titles.
- **`Resource.Error`:**
  Renders `MainErrorSection(errorMessage = brandState.errorMessage, onTryAgain = { ... })`.

---

## 3. API Endpoints & DTOs

### A. Endpoint & Retrofit Service
Defined in `ui/presentation/network/api/ApiEndPoints.kt` & `ApiServices.kt`:
```kotlin
// ApiEndPoints.kt
const val BRANDS = "brands"

// ApiServices.kt
@GET(ApiEndPoints.BRANDS)
suspend fun getAllBrands(): CategoryResponseDto
```

### B. Network DTOs
Backend endpoint schemas for brands and categories are identical, utilizing the shared response DTOs:
Located in `ui/presentation/network/models/response/`:
```kotlin
// CategoryResponseDto.kt
data class CategoryResponseDto(
    @field:SerializedName("metadata") val metadata: MetadataDto? = null,
    @field:SerializedName("data") val data: List<CategoryItemDto>? = null,
    @field:SerializedName("results") val results: Int? = null
)

// CategoryItemDto.kt (network/models/response/common/)
data class CategoryItemDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("image") val image: String? = null,
    @field:SerializedName("slug") val slug: String? = null,
    @field:SerializedName("createdAt") val createdAt: String? = null,
    @field:SerializedName("updatedAt") val updatedAt: String? = null
)
```

### C. Domain Entity
Located in `ui/presentation/commerce/domain/entities/common/CategoryItem.kt`:
```kotlin
data class CategoryItem(
    val image: String? = null,
    val name: String? = null,
    val id: String? = null,
    val category: String? = null
)
```

### D. Mapper
Located in `ui/presentation/commerce/data/mappers/CategoryMapper.kt`:
```kotlin
fun CategoryItemDto.toCategory(): CategoryItem {
    return CategoryItem(
        image = image,
        name = name,
        id = id,
        category = category
    )
}
```

### E. Data Source & Repository Flow
1. **Remote DataSource:** `HomeRemoteDataSource.getBrands(): ApiResult<CategoryResponseDto>`
   - Implementation: `HomeRemoteDataSourceImpl` invokes `safeApiCall { apiServices.getAllBrands() }`.
2. **Repository:** `HomeRepository.getBrands(): ApiResult<List<CategoryItem>>`
   - Implementation: `HomeRepositoryImpl` transforms `CategoryResponseDto.data` items using `categoryItemDto.toCategory()`.
3. **UseCase:** `GetBrandsUseCase @Inject constructor(val homeRepository: HomeRepository)`
   - Implementation: `suspend fun invoke(): ApiResult<List<CategoryItem>> = homeRepository.getBrands()`.
4. **DI Binding:** Centralized in `ui/core/di/CommerceModule.kt`.

---

## 4. Navigation & Entry Points

- **Primary Route:** Hosted in `MainRoute` (`ui/core/navigation/AppRoutes.kt`).
- **Destination:** Rendered in `MainScreen` on tab index `0` (`HomeTab`).
- **Graph Connection:** Initialized and rendered through `AppNavigation.kt`:
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
- Sub-components remain completely stateless and decoupled from the navigation controller.

