# E-Commerce App (Android)

A native Android e-commerce application built with Kotlin and Jetpack Compose, following MVI architecture and Clean Architecture layering, backed by a live e-commerce REST API.

## Features

- Register and login with token-based authentication (session persisted with DataStore)
- Home with categories, brands and product listings
- Product catalog browsing
- Cart: add, update and remove items
- Wishlist: save and remove favorite products

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3), Navigation Compose, Coil
- **Architecture:** MVI (contract + base ViewModel) with Clean Architecture layers per feature (`data` / `domain` / `presentation`)
- **Dependency injection:** Hilt
- **Networking:** Retrofit, OkHttp logging interceptor, Gson, kotlinx.serialization
- **Local storage:** DataStore Preferences

## Project Structure

```
app/src/main/java/com/example/e_commerce_app/
└── ui/
    ├── core/            # shared components, navigation, DI module, MVI base, errors, validation
    └── presentation/    # features: auth, commerce, cart, wishList, network
```

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/AhmedWalied26/E-commerce_App_Android.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and run on an emulator or device (minSdk per `app/build.gradle.kts`).

API base: `ecommerce.routemisr.com` (Route e-commerce training API).
