---
name: write-code
description: >
  MUST be used whenever writing, editing, or generating Kotlin/Gradle code in this project.
  Trigger this skill before any Edit, Write, or code generation action in the Arcyto codebase.
  Contains mandatory Android engineering guidelines, architecture patterns, and project conventions.
---

You MUST follow every guideline below when writing, editing, or generating code in this project. These rules are
mandatory and override any default behavior.

---

## 1. General Kotlin Principles

- **Language level:** Kotlin 2.0.21, JVM target 17.
- Prefer `val` over `var`. Favor immutability everywhere.
- Use expression bodies for single-expression functions.
- Use extension functions for transformations and mappings — not utility classes.
- Prefer `sealed interface` over `sealed class` for state/event hierarchies.
- Use `data class` for value objects; `object` for stateless singletons.
- Use `Result<T>` for error propagation — never throw exceptions across layer boundaries.
- Use Kotlin's standard library functions (`map`, `filter`, `let`, `run`, etc.) idiomatically.
- Never use `!!` — handle nulls explicitly with `?.`, `?:`, or `let`.

---

## 2. Architecture — MVVM + Clean Architecture

Every feature `impl` module must have three layers. Never skip or merge layers.

### 2.1 Data Layer

- **Repository:** Interface + Impl. Impl annotated with `@ContributesBinding(AppScope::class)` and `@Singleton`.
- **Remote Data Source:** Retrofit interface provided via `@Module` + `@Provides` + `@ContributesTo(AppScope::class)`.
- **Mappers:** Top-level extension functions (not classes) in a `mapper/` package. Naming: `toDomain()` or
  `toDomainModel()`.
- **API Models:** Annotated with `@Serializable` (Kotlinx Serialization). Suffix: `*ApiModel`.

### 2.2 Domain Layer

- **Use Cases:** Interface with `suspend operator fun invoke(...)` returning `Result<T>`, plus an `Impl` class.
- Impl annotated with `@ContributesBinding(AppScope::class)`.
- Domain models are plain Kotlin data classes — no framework annotations.
- Use cases contain business logic and coordinate between repositories.

### 2.3 UI Layer

- **ViewModel:** See section 3 below.
- **UiState:** `sealed interface` annotated with `@Immutable`. Must have at least `Loading` (as `object`) and
  `Success` (as `data class`) variants. Add `Error` only if the error state has a distinct UI.
- **UiEvent:** `sealed interface` for one-time events (navigation, snackbar). Use `eventsFlow<T>()` in ViewModel.
- **UiModel:** Separate data classes for UI display. Suffix: `*UiModel`. Never pass domain models directly to
  Composables.
- **Screen Composables:** See section 4 below.

---

## 3. ViewModel Pattern

```kotlin
@ContributesMultibinding(
    scope = MainScreenScope::class,
    boundType = ViewModel::class,
)
@ViewModelKey(MyFeatureViewModel::class)
class MyFeatureViewModel @Inject constructor(
    private val myUseCase: MyUseCase,
) : ViewModel() {

    private val _uiState: StateFlow<MyUiState> = MutableStateFlow(MyUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = viewModelScope.eventsFlow<MyUiEvent>()
    val events: Flow<MyUiEvent> = _events

    fun onSomeAction(param: SomeParam) {
        viewModelScope.launch {
            // handle action
        }
    }
}
```

**Rules:**

- ALWAYS use `@ContributesMultibinding(scope = MainScreenScope::class, boundType = ViewModel::class)`.
- ALWAYS add `@ViewModelKey(YourViewModel::class)`.
- NEVER manually wire ViewModels into Dagger components.
- Use `StateFlow` for persistent state, `eventsFlow<T>()` for one-time events.
- Use `combine()` + `stateIn()` when merging multiple data sources.
- Public functions for UI actions should not be suspending — launch coroutines internally.

---

## 4. Composable Screen Pattern

Every screen has two composables:

### Route Composable (public)

```kotlin
@Composable
fun MyFeatureRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToSomewhere: (RouteRequest) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyFeatureViewModel = arcytoViewModel(),
) {
    val event by viewModel.events.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(event) {
        when (event) {
            is MyUiEvent.Navigate -> navigateToSomewhere(...)
            is MyUiEvent.ShowSnackbar -> onShowSnackbar(...)
            null
            -> {}
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MyFeatureScreen(uiState, viewModel::onSomeAction, modifier)
}
```

### Screen Composable (internal, pure)

```kotlin
@Composable
internal fun MyFeatureScreen(
    uiState: MyUiState,
    onAction: (ActionParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Pure UI — no ViewModel, no side effects
    when (uiState) {
        MyUiState.Loading -> LoadingState(modifier)
        is MyUiState.Success -> ContentState(uiState, onAction, modifier)
    }
}
```

**Rules:**

- Route handles ViewModel injection via `arcytoViewModel()`.
- Route handles events via `LaunchedEffect`.
- Use `collectAsStateWithLifecycle()` — never plain `collectAsState()`.
- Screen composable is `internal`, stateless, and testable.
- Pass actions as lambda callbacks — never pass the ViewModel to Screen.
- Always include `modifier: Modifier = Modifier` as the last default parameter.

---

## 5. Dependency Injection (Dagger + Anvil)

| What                   | Annotation                                                                                         |
|------------------------|----------------------------------------------------------------------------------------------------|
| Bind interface to impl | `@ContributesBinding(AppScope::class)` on impl class                                               |
| Register ViewModel     | `@ContributesMultibinding(MainScreenScope::class, boundType = ViewModel::class)` + `@ViewModelKey` |
| Provide via factory    | `@Module` + `@ContributesTo(AppScope::class)` + `@Provides`                                        |
| Singleton              | `@Singleton` on class or `@Provides` method                                                        |
| Qualify dispatchers    | `@Dispatcher(IO)`, `@Dispatcher(Main)`, `@Dispatcher(Default)`                                     |

**Rules:**

- NEVER create manual Dagger `@Component` or `@Subcomponent` for features — Anvil handles component merging.
- Available scopes: `AppScope` (singleton) and `MainScreenScope` (per-screen).
- Use `class` for modules that need constructor params; `object` for stateless modules.
- Retrofit interfaces are provided via `retrofit.create()` in a `@Provides` method.

---

## 6. Module & Package Structure

### New Feature Checklist

1. `feature/<name>/api/` — Navigation contracts only (routes, exposed interfaces).
2. `feature/<name>/impl/` — Full implementation.
3. Package: `io.github.msh91.arcyto.<featurename>`.
4. Sub-packages inside impl: `ui/`, `domain/usecase/`, `data/repository/`, `data/mapper/`, `data/model/`, `di/`.

### build.gradle.kts for feature impl

```kotlin
plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)  // only if needed
}

android {
    namespace = "io.github.msh91.arcyto.<featurename>"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.formatter)
    implementation(projects.core.tooling.extension)

    implementation(libs.bundles.coroutines)
    implementation(libs.navigation.compose)

    api(projects.feature.< name >.api)

    testImplementation(projects.core.tooling.test)
}
```

**Rules:**

- NEVER hardcode dependency versions — always use `libs.*` from version catalog.
- Use `api()` for the feature's own API module; `implementation()` for everything else.
- Add `core:data:remote` or `core:data:local` only if the feature needs network/storage.

---

## 7. Naming Conventions (Mandatory)

| Type                 | Pattern              | Example                                       |
|----------------------|----------------------|-----------------------------------------------|
| ViewModel            | `*ViewModel`         | `HistoricalListViewModel`                     |
| Use case interface   | `*UseCase`           | `GetHistoricalChartUseCase`                   |
| Use case impl        | `*UseCaseImpl`       | `GetHistoricalChartUseCaseImpl`               |
| Repository interface | `*Repository`        | `HistoricalChartRepository`                   |
| Repository impl      | `*RepositoryImpl`    | `HistoricalChartRepositoryImpl`               |
| Remote data source   | `*RemoteDataSource`  | `HistoricalRemoteDataSource`                  |
| Local data source    | `*LocalDataSource`   | `HistoryLocalDataSource`                      |
| API model            | `*ApiModel`          | `HistoricalChartApiModel`                     |
| Domain model         | Plain name           | `HistoricalPrice`, `CoinDetails`              |
| UI model             | `*UiModel`           | `PriceValueUiModel`                           |
| UI state             | `*UiState`           | `HistoryUiState`                              |
| UI event             | `*UiEvent`           | `HistoricalListUiEvent`                       |
| Mapper file          | `*Mapper.kt`         | `HistoricalDataMapper.kt`                     |
| DI module            | `*Module`            | `HistoryModule`                               |
| Screen composable    | `*Screen` / `*Route` | `HistoricalListScreen`, `HistoricalListRoute` |
| Test class           | `*Test`              | `HistoricalListViewModelTest`                 |

---

## 8. Testing

When writing or modifying code, also write/update corresponding tests. Use the `/write-tests` skill for full testing
guidelines, patterns, and run commands.

---

## 9. Error Handling

- Wrap API/IO calls in `Result<T>` — use `runCatching {}` or explicit try/catch mapped to `Result.failure()`.
- Use `CompositeErrorMapper` to convert exceptions to user-facing messages.
- In ViewModel `catch` blocks, map errors and emit events.

---

## 10. Code Style

- No wildcard imports.
- Organize imports: Kotlin stdlib, Android/Compose, third-party, project.
- Use trailing commas in parameter lists and collection literals.
- Use `internal` visibility for implementation details that shouldn't leak outside the module.
- Prefer named arguments for functions with multiple parameters of the same type.
- Use `@Immutable` on all `UiState` and `UiModel` classes used in Compose.

## 11. Technology Stack

| Category      | Technology                                     |
|---------------|------------------------------------------------|
| Language      | Kotlin 2.0, JVM 17                             |
| UI            | Jetpack Compose, Material3                     |
| DI            | Dagger + Anvil                                 |
| Networking    | Retrofit + OkHttp                              |
| Serialization | Kotlinx Serialization                          |
| Local storage | DataStore                                      |
| Async         | Coroutines, Flow                               |
| Image loading | Coil 2.6.0                                     |
| Testing       | JUnit4, MockK, Truth, Turbine, Coroutines Test |

