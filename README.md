# 🍹 Cocktail App - Clean Architecture & Jetpack Compose

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-green)
![Architecture](https://img.shields.io/badge/Clean-Architecture-blue)
![Status](https://img.shields.io/badge/Maintained-Yes-brightgreen)

A modern Android application built to demonstrate **Clean Architecture**, **MVVM**, and **Offline-first** capabilities using Jetpack Compose. The app fetches random cocktail recipes from an external API and caches the data locally to ensure a seamless user experience even without an internet connection.

## ✨ Features

*   **🎲 Random Drink Generator:** Fetches a random cocktail recipe with a single tap.
*   **📶 Offline Support (Offline-First):** Automatically caches the last viewed drink. If the network fails, the app gracefully falls back to the local database (Room) to show the last known recipe.
*   **♿ Accessibility Focus:**
    *   **TalkBack Support:** Optimized semantic descriptions and groupings.
    *   **Live Regions:** Error messages are announced automatically when they appear.
    *   **Smart Focus Management:** Focus automatically shifts to the new image when a drink loads using `FocusRequester`.
*   **🌑 Dark/Light Mode:** Fully compatible with Material 3 theming.
*   **🧪 Comprehensive Testing:** Unit tests covering Domain, Data, and Presentation layers.

## 🛠 Tech Stack

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
*   **Architecture:** Clean Architecture (Presentation, Domain, Data) + MVVM
*   **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
*   **Network:** [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/)
*   **Local Database:** [Room](https://developer.android.com/training/data-storage/room)
*   **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
*   **Async:** Coroutines & Flow

## 🏛 Architecture Overview

The project follows the **Clean Architecture** principles to ensure separation of concerns and testability.

1.  **Presentation Layer:**
    *   **Composables:** `DrinkScreen` handles the UI and Accessibility logic.
    *   **ViewModel:** `DrinkViewModel` manages the UI State (`Loading`, `Success`, `Error`) and communicates with Use Cases.
2.  **Domain Layer:**
    *   **Models:** Pure Kotlin data classes (`Drink`).
    *   **Use Cases:** Encapsulate business logic (e.g., `GetRandomDrinkUseCase`, `GetLastDrinkShowedUseCase`).
    *   **Repository Interface:** Defines the contract for data operations.
3.  **Data Layer:**
    *   **Repository Implementation:** Handles the logic of choosing between Remote (API) and Local (Room) data sources.
    *   **Mappers:** Transforms DTOs (Data Transfer Objects) to Domain models.

## ✅ Testing Strategy

The project includes rigorous unit testing using **Mockk** and **Turbine**.

*   **Domain Tests:** Verify that Use Cases call the repository correctly.
*   **Data Tests:** Verify that the Repository implementation orchestrates API calls and DB caching correctly, handling DTO mapping.
*   **ViewModel Tests:** Verify UI State transitions (Loading -> Success/Error) using `MainDispatcherRule` and `Turbine` for Flow testing.

## ♿ Accessibility Highlights

Special attention was paid to make the app accessible for blind or visually impaired users:

*   **Focus Requester:** Programmatically moves focus to the main content when data finishes loading.
*   **Merge Descendants:** Ingredients and measurements are merged into single semantic nodes for smoother reading.
*   **Live Regions:** Network errors use `LiveRegionMode.Polite` to inform the user without interrupting navigation.
