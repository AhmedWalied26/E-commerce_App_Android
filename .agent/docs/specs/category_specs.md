# Feature Specification: Categories (`@commerce`, `@network`, `@di`)

## 1. Overview & User Flows

The Categories feature provides a catalog of product categories displayed directly on the Home dashboard of the app.

### Key User Flows:
1. **Home Feed Category Display:**
   - When the user lands on the Home tab (`HomeTab`) inside `MainScreen`, the app automatically dispatches `HomeIntent.LoadCategory`.
   - Displays a 2-row horizontally scrolling grid (`LazyHorizontalGrid`) of circular category images and category titles.
   - Images are loaded asynchronously with Coil (`SubcomposeAsyncImage`) with a nested `MainLoadingSection()` indicator.
2. **Error Recovery & Retry:**
   - If network or server failure occurs, the category section renders `MainErrorSection`.
   - The user can tap "Try again" to re-trigger category fetching.
3. **Category Navigation / Expansion:**
   - Includes a "view all" action label designed for category exploration.

---

## 2. MVI Contract & Screen State

Category logic is managed through the Home screen MVI contract:
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
The category feed switches between the standard UI states:
- **`Resource.Initial` & `Resource.Loading`:**
  Renders `MainLoadingSection()` in place of the grid.
- **`Resource.Success<List<CategoryItem>>`:**
  Renders `HomeSuccessSection(list = categoryState.data)`.
  - Grid: `LazyHorizontalGrid(rows = GridCells.Fixed(2), modifier = Modifier.height(260.dp))`
  - Item layout: Circular clipped image (`100.dp`, `CircleShape`) and regular text (`regular14DarkBlue`).
- **`Resource.Error`:**
  Renders `MainErrorSection(errorMessage = categoryState.errorMessage, onTryAgain = { ... })`.

---

## 3. API Endpoints & DTOs

### A. Endpoint & Retrofit Service
Defined in `ui/presentation/network/api/ApiEndPoints.kt` & `ApiServices.kt`:
```kotlin
// ApiEndPoints.kt
const val CATEGORIES = "categories"

// ApiServices.kt
@GET(ApiEndPoints.CATEGORIES)
suspend fun getAllCategories(): CategoryResponseDto
```

### B. Network DTOs
Located in `ui/presentation/network/models/response/`:
```kotlin
// CategoryResponseDto.kt
data class CategoryResponseDto(
    @field:SerializedName("metadata")
    val metadata: MetadataDto? = null,
    @field:SerializedName("data")
    val data: List<CategoryItemDto>? = null,
    @field:SerializedName("results")
    val results: Int? = null
)

// CategoryItemDto.kt (network/models/response/common/)
data class CategoryItemDto(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("image") val image: String? = null,
    @field:SerializedName("slug") val slug: String? = null,
    @field:SerializedName("category") val category: String? = null,
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
1. **Remote DataSource:** `HomeRemoteDataSource.getCategory(): ApiResult<CategoryResponseDto>`
   - Implementation: `HomeRemoteDataSourceImpl` invokes `safeApiCall { apiServices.getAllCategories() }`.
2. **Repository:** `HomeRepository.getCategory(): ApiResult<List<CategoryItem>>`
   - Implementation: `HomeRepositoryImpl` unpacks `CategoryResponseDto` and maps each DTO via `categoryItemDto.toCategory()`.
3. **UseCase:** `GetCategoriesUseCase @Inject constructor(val homeRepository: HomeRepository)`
   - Single-action call: `suspend fun invoke(): ApiResult<List<CategoryItem>> = homeRepository.getCategory()`.
4. **DI Binding:** Centralized in `ui/core/di/CommerceModule.kt`.

---

## 4. Navigation & Entry Points

- **Primary Route:** Hosted within `MainRoute` (`ui/core/navigation/AppRoutes.kt`).
- **Destination:** Rendered in `MainScreen` (`ui/presentation/commerce/feature/main/MainScreen.kt`) when bottom bar tab index is `0` (`HomeTab`).
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
- Screen components do not receive `NavController`, ensuring high reusability and previewability.

