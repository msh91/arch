# CLAUDE.md — Arcyto Android Project

This file provides guidance for AI assistants working on the Arcyto codebase.

## Project Overview

Arcyto is a modular Android demo app that showcases cryptocurrency data via the CoinGecko API. It uses Jetpack Compose for UI, Dagger/Anvil for DI, and Kotlin Coroutines/Flow for async operations.

## Required Setup

Before building, create `local.properties` in the project root:
```
API_KEY=YOUR_COINGECKO_API_KEY
```
This is consumed at compile time by `core/data/remote/build.gradle.kts` to generate `BuildConfig.API_KEY`.

## Build & Run Commands

```bash
./gradlew build           # Full build + tests
./gradlew test            # Unit tests only
./gradlew assembleDebug   # Debug APK
./gradlew assembleRelease # Release APK (minified)
```

## Module Structure

```
app/                          # Application entry point
build-logic/convention/       # Gradle convention plugins (DRY build config)
core/
  data/local/                 # DataStore preferences
  data/remote/                # Retrofit/OkHttp API layer + API key
  design-system/              # Compose UI components & Material3 theme
  di/                         # Dagger/Anvil scopes, ViewModel base, dispatchers
  formatter/                  # Price and date formatting use cases
  tooling/extension/          # Kotlin extension utilities
  tooling/test/               # Shared test dependencies (re-exported)
feature/
  history/api/                # Navigation contracts for history feature
  history/impl/               # History feature implementation
  details/api/                # Navigation contracts for details feature
  details/impl/               # Details feature implementation
```

### api/impl Module Pattern

Features are split into two Gradle modules:
- **`api/`** — Public contracts only: navigation routes, exposed interfaces. Other modules depend on this.
- **`impl/`** — Full implementation: UI, ViewModel, domain, data, DI. Depends on its own `api/`.

This prevents cyclic dependencies and keeps implementation details private.

## Architecture

Layers within each feature `impl` module:
- **UI** — Composable screens, `*UiState` sealed classes, `*UiEvent` sealed classes
- **Domain** — Use cases (`*UseCase` interface + `*UseCaseImpl`), domain models
- **Data** — Repository (`*Repository` interface + `*RepositoryImpl`), remote/local data sources, API models, mappers

Pattern: `MVVM + Clean Architecture`

## Technology Stack

| Category | Technology |
|---|---|
| Language | Kotlin 2.0.21, JVM 17 |
| UI | Jetpack Compose (BOM 2025.02.00), Material3 |
| DI | Dagger 2.52 + Anvil 2.5.0-beta11 |
| Networking | Retrofit 2.11.0 + OkHttp 4.12.0 |
| Serialization | Kotlinx Serialization 1.7.3 |
| Local storage | DataStore 1.1.3 |
| Async | Coroutines 1.9.0, Flow |
| Image loading | Coil 2.6.0 |
| Testing | JUnit4, MockK, Truth, Turbine, Coroutines Test |

## Dependency Management

All versions are centralized in `gradle/libs.versions.toml` (version catalog). Use this file to add or update dependencies — do not hardcode versions in individual `build.gradle.kts` files.

Convention plugins in `build-logic/convention/` are applied per-module:
- `arcyto.android.app` — for the `app` module
- `arcyto.android.library` — for Android library modules
- `arcyto.anvil.library` — for modules using Anvil DI

## Dependency Injection Patterns

- Anvil `@ContributesTo`, `@ContributesBinding`, `@ContributesMultibinding` annotations handle DI wiring at compile time.
- Scopes defined in `core/di`: `AppScope` (singleton), `MainScreenScope` (per-screen).
- ViewModels are registered via `@ContributesMultibinding` into `ViewModelProviderFactory`.
- Never use manual `@Inject constructor` on ViewModels — use the Anvil multibinding pattern.

## Naming Conventions

| Type | Pattern |
|---|---|
| Package root | `io.github.msh91.arcyto` |
| ViewModel | `*ViewModel` |
| Use case | `*UseCase` (interface) / `*UseCaseImpl` |
| Repository | `*Repository` (interface) / `*RepositoryImpl` |
| Data source | `*RemoteDataSource` / `*LocalDataSource` |
| Mapper | `*Mapper` |
| Composable screen | `*Screen` |
| UI state | `*UiState` |
| UI events | `*UiEvent` |
| DI module | `*Module` |

## Testing Conventions

- **Location:** `src/test/kotlin/` for unit tests; `src/androidTest/kotlin/` for instrumented tests.
- **Libraries:** Import via `core:tooling:test` — it re-exports JUnit4, MockK, Truth, Turbine, Coroutines Test.
- **Dispatchers:** Use `MainDispatcherRule` from `core:tooling:test` in ViewModel tests.
- **Mocking:** Use MockK (`mockk()`, `coEvery`, `coVerify`).
- **Assertions:** Prefer Google Truth (`assertThat(...).isEqualTo(...)`).
- **Flow testing:** Use Turbine (`flow.test { ... }`).
- Test class naming: `*Test` suffix matching the class under test.

## CI/CD

GitHub Actions workflow (`.github/workflows/build.yml`) runs on push and PRs:
1. Sets up JDK 17 (Amazon Corretto)
2. Caches Gradle dependencies
3. Runs `./gradlew build` (build + unit tests)

The `API_KEY` is not required in CI — the remote module's `build.gradle.kts` uses an empty fallback if not present in `local.properties`.

## Key Files for Navigation

| File | Purpose |
|---|---|
| `app/src/main/kotlin/.../ArcytoApp.kt` | Application class, Dagger component init |
| `app/src/main/kotlin/.../MainActivity.kt` | Single activity, Compose host |
| `app/src/main/kotlin/.../ArcytoNavHost.kt` | Navigation graph |
| `core/di/` | Shared DI infrastructure |
| `core/design-system/` | All reusable Compose components |
| `gradle/libs.versions.toml` | All dependency versions |
| `build-logic/convention/` | Shared Gradle build configuration |

## Adding a New Feature

1. Create `feature/<name>/api/` module with navigation contracts.
2. Create `feature/<name>/impl/` module with UI/domain/data layers.
3. Add both modules to `settings.gradle.kts`.
4. Apply `arcyto.android.library` (and `arcyto.anvil.library` if using DI) in each module's `build.gradle.kts`.
5. Register navigation route in `ArcytoNavHost.kt`.
6. Wire DI using Anvil annotations — no manual component wiring needed.
7. Add unit tests under `src/test/kotlin/` using `core:tooling:test` dependency.
