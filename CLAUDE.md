# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Arcyto is a modular Android demo app that showcases cryptocurrency data via the CoinGecko API. It uses Jetpack Compose for UI, Dagger/Anvil for DI, and Kotlin Coroutines/Flow for async operations.

## Development Workflow

Follow these steps for every code change or task:

1. **Make changes** — Implement the requested feature, fix, or refactoring.
2. **Verify build passes** — Run `./gradlew assembleDebug` and ensure it succeeds.
3. **Update unit tests** — Add new tests for new/changed code if it is unit testable. Update existing tests that are affected by the changes and ensure all tests pass.
4. **Lint check** — Run `ktlint -F` to check for lint violations and auto-format.

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
```

### api/impl Module Pattern

Features are split into two Gradle modules:

- **`api/`** — Public contracts only: navigation routes, exposed interfaces. Other modules depend on this.
- **`impl/`** — Full implementation: UI, ViewModel, domain, data, DI. Depends on its own `api/`.

This prevents cyclic dependencies and keeps implementation details private.

## Dependency Management

All versions are centralized in `gradle/libs.versions.toml` (version catalog). Use this file to add or update dependencies — do not hardcode versions in individual `build.gradle.kts` files.

Convention plugins in `build-logic/convention/` are applied per-module:

- `arcyto.android.app` — for the `app` module
- `arcyto.android.library` — for Android library modules
- `arcyto.anvil.library` — for modules using Anvil DI

## Naming Conventions

| Type              | Pattern                                       |
|-------------------|-----------------------------------------------|
| Package root      | `io.github.msh91.arcyto`                      |
| ViewModel         | `*ViewModel`                                  |
| Use case          | `*UseCase` (interface) / `*UseCaseImpl`       |
| Repository        | `*Repository` (interface) / `*RepositoryImpl` |
| Data source       | `*RemoteDataSource` / `*LocalDataSource`      |
| Mapper            | `*Mapper`                                     |
| Composable screen | `*Screen`                                     |
| UI state          | `*UiState`                                    |
| UI events         | `*UiEvent`                                    |
| DI module         | `*Module`                                     |
