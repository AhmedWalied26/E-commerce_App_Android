# MVI & Clean Architecture Rules

This document outlines the architectural standards and conventions for the project. Every contributor and agent working on this codebase must strictly adhere to these rules.

---

## 1. Project Structure & Centralized Placement

We follow a **Centralized Clean Architecture + MVI** pattern. Features do **not** own isolated networking, navigation, or dependency injection modules. All shared infrastructure is strictly centralized.

### Root Package
`com.example.e_commerce_app` (or alias `com.example.ecommerceapp`)

### Directory Tree & Ownership
```text
app/src/main/java/com/example/e_commerce_app/
├── ui/
│   ├── core/                                # Shared foundational utilities & components
│   │   ├── components/                      # Design system (CustomButton, CustomTextField, CustomAppBar, ImageSlider)
│   │   ├── di/                              # Feature bindings (CommerceModule.kt)
│   │   ├── errors/                          # Domain & network error models (AppErrors.kt)
│   │   ├── mvi/                             # Core MVI foundations (BaseViewModel.kt, MviContract.kt)
│   │   ├── navigation/                      # Centralized navigation (AppRoutes.kt, AppNavigation.kt)
│   │   └── utils/                           # Validators & helpers (AppValidation.kt)
│   ├── presentation/
│   │   ├── common/                          # Standardized UI state views (MainLoadingSection, MainErrorSection, MainEmptyListSection)
│   │   ├── network/                         # Centralized networking layer
│   │   │   ├── api/                         # ApiEndPoints.kt, ApiServices.kt, NetworkModule.kt
│   │   │   ├── models/                      # Centralized DTOs (request/ and response/)
│   │   │   └── utilty/                      # ApiResult.kt, SafeApiCall.kt
│   │   ├── auth/                            # Auth feature (data, domain, feature/login, feature/register)
│   │   ├── cart/                            # Cart feature (data, domain, feature/cart)
│   │   └── commerce/                        # Commerce feature (data, domain, feature/main tabs & subfeatures)
```

### Strict Centralization Invariants
1. **Zero Isolated `di/` inside features:**
   - All Hilt `@Binds` for repositories and data sources live in centralized modules: `ui/core/di/CommerceModule.kt`.
   - All Retrofit, OkHttp, and API service `@Provides` live in `ui/presentation/network/api/NetworkModule.kt`.
2. **Zero Isolated `navigation/` inside features:**
   - All navigation routes must be `@Serializable` objects or data classes declared in `ui/core/navigation/AppRoutes.kt`.
   - All screen composable destinations are plugged into `ui/core/navigation/AppNavigation.kt`.
3. **Zero Isolated `api/` or network DTOs inside features:**
   - Base URLs and endpoint paths belong in `ui/presentation/network/api/ApiEndPoints.kt`.
   - Retrofit endpoint functions belong in `ui/presentation/network/api/ApiServices.kt`.
   - Request and response DTOs belong in `ui/presentation/network/models/`.
4. **Mandatory Reuse of Common & Core Components:**
   - **Loading State:** Must use `MainLoadingSection()` from `ui/presentation/common/MainLoadingSection.kt`.
   - **Error State:** Must use `MainErrorSection(errorMessage, onTryAgain)` from `ui/presentation/common/MainErrorSection.kt`.
   - **Empty State:** Must use `MainEmptyListSection()` from `ui/presentation/common/MainEmptyListSection.kt`.
   - **Buttons & Text Fields:** Must use `CustomButton` (or `CustomElevatedButton`) and `CustomTextField` from `ui/core/components/`.
   - **Validation & Error Handling:** Must use `AppValidation` and `AppErrors` from `ui/core/`.

---

## 2. Naming & Data Purity Standards

### Domain Layer Purity
- **Zero Third-Party Annotations:** Domain entities (`domain/entities/`) must be pure Kotlin classes. No `@SerializedName`, `@Parcelize`, Android framework types, or network libraries.
- **Repository Contracts:** Domain repository interfaces must return `ApiResult<T>`, where `T` is a pure Domain model:
  ```kotlin
  interface CartRepository {
      suspend fun getCart(): ApiResult<Cart>
  }
  ```
- **Single-Action UseCases:**
  - One UseCase per specific action.
  - Must define `suspend operator fun invoke(...)` to enable callable syntax (`getCartUseCase()`).

### Mapper Conventions
- **Explicit Domain Target Naming:** Mapper functions must be named after their target domain model, **never** generic names like `toDomain()`.
  - ✅ `CategoryItemDto.toCategory(): CategoryItem`
  - ✅ `ProductItemDto.toProductItem(): ProductItem`
  - ✅ `CartResponseDto.toCart(): Cart`
  - ❌ `ProductDto.toDomain(): Product`
- **Location:** Place mappers in `data/mappers/` inside the respective feature module.

### DTO Separation Rules
- **Separate DTOs for ID vs. Expanded Object:** Never use a single polymorphic or ambiguous DTO for fields that vary across endpoints.
  - If endpoint A returns an ID as a `String` (e.g., adding to cart/wishlist returns `{ "product": "6407f... " }`), create an ID-specific DTO: `CartMutationResponseDto` / `AddToCartResponseDto`.
  - If endpoint B returns the fully populated object (e.g., fetching cart returns `{ "product": { "_id": "...", "title": "..." } }`), use a detailed item DTO: `CartProductDto`.

---

## 3. Presentation & BaseViewModel Architecture Standards

### Contract Definition with `UiStates`, `UiIntent`, `UiEffects`
All screen contracts are defined in the feature's `view_model/` package and **must implement** the core interfaces from `com.example.e_commerce_app.ui.core.mvi`:

1. **`<Feature>States` (Data class implementing `UiStates`):**
   - Represents the complete UI state at any moment.
   - Typically contains fields typed as `Resource<T>` (`Initial`, `Loading`, `Success`, `Error`).
   ```kotlin
   data class CartStates(
       val cartState: Resource<Cart> = Resource.Initial
   ) : UiStates
   ```
2. **`<Feature>Intent` (Sealed interface implementing `UiIntent`):**
   - Represents user interactions or lifecycle triggers (e.g., `LoadCart`, `UpdateQuantity`).
   ```kotlin
   sealed interface CartIntent : UiIntent {
       data object LoadCart : CartIntent
       data class UpdateProductQuantity(val productId: String, val count: Int) : CartIntent
   }
   ```
3. **`<Feature>Effects` (Sealed interface implementing `UiEffects`):**
   - Represents one-off side effects that must not survive configuration changes (e.g., `ShowToast`, `NavigateToCheckout`).
   ```kotlin
   sealed interface CartEffects : UiEffects {
       data class ShowToast(val message: String) : CartEffects
       data object NavigateToCheckout : CartEffects
   }
   ```

---

### Mandatory `BaseViewModel` Implementation Rules
Every ViewModel in the project **must inherit** from `BaseViewModel`:
```kotlin
abstract class BaseViewModel<States : UiStates, Intent : UiIntent, Effects : UiEffects>(initialValue: States)
```

#### Strict ViewModel Rules:
1. **Extend BaseViewModel with Initial State:**
   ```kotlin
   @HiltViewModel
   class CartViewModel @Inject constructor(
       private val getCartUseCase: GetCartUseCase
   ) : BaseViewModel<CartStates, CartIntent, CartEffects>(CartStates()) { ... }
   ```
2. **State Reads via `currentState`:**
   - Always read the latest state synchronously using `currentState` (e.g., `currentState.cartState`), never by creating ad-hoc state holders.
3. **State Mutations via `updateState { copy(...) }`:**
   - Never mutate state directly or manage private `_states` flows in child ViewModels.
   - Use the protected helper:
     ```kotlin
     updateState { copy(cartState = Resource.Loading) }
     ```
4. **Side Effects via `sendEffects { ... }`:**
   - Never manage private `_effects` channels in child ViewModels.
   - Emit one-time effects using the protected lambda builder:
     ```kotlin
     sendEffects { CartEffects.ShowToast("Item added successfully") }
     ```
5. **Intent Processing via `override fun processIntent(intent: <Feature>Intent)`:**
   - Exhaustive `when (intent)` expression inside `override fun processIntent(intent: <Feature>Intent)`:
     ```kotlin
     override fun processIntent(intent: CartIntent) {
         when (intent) {
             CartIntent.LoadCart -> loadCart()
             is CartIntent.UpdateProductQuantity -> updateQuantity(intent.productId, intent.count)
         }
     }
     ```

---

### Composable Screen Separation & Consumption

Every screen must strictly separate stateful container from stateless content:

#### 1. Outer Stateful Composable (`<Feature>Screen`):
- Injected with `viewModel: <Feature>ViewModel = hiltViewModel()`.
- **State Collection:** Collected via `val state by viewModel.states.collectAsState()` (or `collectAsStateWithLifecycle()`).
- **Effect Collection:** Collected via `viewModel.effects.CollectEffect { effect -> when (effect) { ... } }`:
  ```kotlin
  viewModel.effects.CollectEffect { effect ->
      when (effect) {
          is CartEffects.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
          CartEffects.NavigateToCheckout -> onNavigateToCheckout()
      }
  }
  ```
- **Action Dispatching:** Calls `viewModel.processIntent(...)` (e.g., `onIntent = viewModel::processIntent`).
- **Zero `NavController` in Leaf Screens:** Leaf screens receive only navigation lambdas (e.g., `onBackClick: () -> Unit`), never `NavController`.

#### 2. Inner Stateless Content Composable (`<Feature>Content`):
- Pure UI function with signature:
  ```kotlin
  @Composable
  fun CartContent(
      modifier: Modifier = Modifier,
      state: CartStates,
      onIntent: (CartIntent) -> Unit
  )
  ```
- Renders `MainLoadingSection`, `MainErrorSection`, `MainEmptyListSection`, or success views based on `state`.
- Enables instant Compose Previews, UI tests, and decoupling from ViewModel and Android lifecycle.
