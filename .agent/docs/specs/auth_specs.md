# Feature Specification: Authentication (`@auth`)

## 1. Overview & User Flows

The Authentication feature handles user identification, onboarding state, credential verification, and local token persistence.

### Key User Flows:
1. **User Sign In (Login):**
   - User enters email and password on the `LoginScreen`.
   - Client validates fields; upon tapping "Login" (`OnLoginClicked`), the app triggers `login()` in `LoginViewModel`.
   - On API success (`200 OK`), the bearer JWT token is securely saved via `AuthLocalDataSource.saveToken(token)`.
   - `LoginEffects.OnNavigateToHome` triggers navigation to `MainRoute`, clearing the login screen from the backstack (`popUpTo(LoginRoute) { inclusive = true }`).
   - If an error occurs, `Resource.Error` updates state and `LoginEffects.ShowToast` displays the server error message.
2. **User Sign Up (Registration):**
   - Accessible via "Create Account" on the login screen.
   - User provides Name, Phone, Email, Password, and Password Confirmation.
   - Validations ensure proper format via `AppValidation`.
   - On tapping "Sign Up" (`OnSignUpClicked`), registration request is dispatched.
   - On success, the returned token is saved locally, and navigation moves to `MainRoute`.
3. **Session & Token Management:**
   - Token is stored/cleared using `AuthLocalDataSource`.
   - Logout from `MainScreen` clears session data and routes back to `LoginRoute`.
   - First-time launch checks are supported via `CheckIsFirstTimeUseCase` and `SetFirstTimeUseCase`.

---

## 2. MVI Contract & Screen State

### A. Login Feature Contract
Located in: `ui/presentation/auth/feature/login/view_model/`

#### 1. States (`LoginStates.kt`)
```kotlin
data class LoginStates(
    val email: String = "ahmed_route@gmail.com",
    val password: String = "Ahmed@123",
    val loginState: Resource<Unit> = Resource.Initial
)
```

#### 2. Intent (`LoginViewModel.kt`)
```kotlin
sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object OnLoginClicked : LoginIntent
    data object OnCreateAccountClicked : LoginIntent
}
```

#### 3. Effects (`LoginViewModel.kt`)
```kotlin
sealed interface LoginEffects {
    data class ShowToast(val message: String) : LoginEffects
    data object OnNavigateToRegister : LoginEffects
    data object OnNavigateToHome : LoginEffects
}
```

#### 4. UI State Handling
- **Initial:** Renders empty/prefilled credential fields and an active `CustomButton`.
- **Loading:** Button shows `MainLoadingSection()` indicator and disables interactions.
- **Success:** Triggers navigation effect to `MainRoute`.
- **Error:** Emits `ShowToast` effect with the error message and resets button to interactive.

---

### B. Register Feature Contract
Located in: `ui/presentation/auth/feature/register/view_model/`

#### 1. States (`RegisterStates.kt`)
```kotlin
data class RegisterStates(
    val name: String = "ahmed",
    val phone: String = "00110011001",
    val email: String = "ahmed0@route.com",
    val password: String = "Ahmed@123",
    val rePassword: String = "Ahmed@123",
    val registerState: Resource<Unit> = Resource.Initial
)
```

#### 2. Intent (`RegisterViewModel.kt`)
```kotlin
sealed interface RegisterIntent {
    data class NameChanged(val name: String) : RegisterIntent
    data class EmailChanged(val email: String) : RegisterIntent
    data class PhoneChanged(val phone: String) : RegisterIntent
    data class PasswordChanged(val password: String) : RegisterIntent
    data class RePasswordChanged(val rePassword: String) : RegisterIntent
    data object OnSignUpClicked : RegisterIntent
}
```

#### 3. Effects (`RegisterViewModel.kt`)
```kotlin
sealed interface RegisterEffects {
    data class ShowToast(val message: String) : RegisterEffects
    data object OnNavigateToHome : RegisterEffects
}
```

---

## 3. API Endpoints & DTOs

### A. Endpoints & Retrofit Service
Defined in `ui/presentation/network/api/ApiEndPoints.kt` & `ApiServices.kt`:
```kotlin
// ApiEndPoints.kt
const val LOGIN = "auth/signin"
const val REGISTER = "auth/signup"

// ApiServices.kt
@POST(ApiEndPoints.LOGIN)
suspend fun login(@Body loginRequest: LoginRequestDto): AuthResponseDto

@POST(ApiEndPoints.REGISTER)
suspend fun register(@Body registerRequest: RegisterRequestDto): AuthResponseDto
```

### B. DTO Models
- **Request DTOs:**
  - `LoginRequestDto(email, password)` (`ui/presentation/network/models/request/auth/login/`)
  - `RegisterRequestDto(name, phone, email, password, rePassword)` (`ui/presentation/network/models/request/auth/register/`)
- **Response DTOs:**
  - `AuthResponseDto(message: String?, user: UserDto?, token: String?)` (`ui/presentation/network/models/response/auth/`)
  - `UserDto(name: String?, email: String?, role: String?)`

### C. Domain Entities & Mappers
- **Domain Entities:** `LoginRequest`, `RegisterRequest` (located in `ui/presentation/auth/domain/entities/`)
- **Mappers:** Located in `ui/presentation/auth/data/mappers/`
  - `LoginRequest.toLoginRequestDto(): LoginRequestDto`
  - `RegisterRequest.toRegisterRequestDto(): RegisterRequestDto`

### D. DataSources & Repository
- `AuthRemoteDataSource` / `AuthRemoteDataSourceImpl` wraps Retrofit calls in `safeApiCall`.
- `AuthLocalDataSource` / `AuthLocalDataSourceImpl` handles token storage and first-time flag.
- `AuthRepository` / `AuthRepositoryImpl` coordinates remote calls with local persistence.
- **DI Registration:** Bound in `ui/core/di/CommerceModule.kt`.

---

## 4. Navigation & Entry Points

### Routes
Defined in `ui/core/navigation/AppRoutes.kt`:
```kotlin
@Serializable
object LoginRoute

@Serializable
object RegisterRoute
```

### Destinations & Graph Navigation
Wired in `ui/core/navigation/AppNavigation.kt`:
```kotlin
composable<LoginRoute> {
    LoginScreen(
        onLoginClick = {
            onSplashFinished()
            navController.navigate(MainRoute) {
                popUpTo(LoginRoute) { inclusive = true }
            }
        },
        onCreateAccount = {
            navController.navigate(RegisterRoute)
        }
    )
}

composable<RegisterRoute> {
    RegisterScreen(
        onSignUpClick = {
            navController.navigate(MainRoute) {
                popUpTo(RegisterRoute) { inclusive = true }
            }
        }
    )
}
```
Screen composables receive zero `NavController` references—all transitions are handled via callback lambdas.

