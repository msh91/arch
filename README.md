# Arcyto

## Summary

This project is a demo application to showcase my skills in Android development, both infrastructure and feature development.
This is a demo Android app to showcase my skills in infrastructure and feature development. 
It fetches historical price data using the [CoinGecko](https://www.coingecko.com) API and displays it in a list, and displays the price details of a coin for the selected date.

I aimed to showcase what a real-world project structure looks like, using a modular structure and clean architecture with MVVM for the presentation layer.

## Build and Run

To be able to build and run the app, you need to obtain an api key
from [CoinGecko](https://docs.coingecko.com/reference/introduction)
and add it to `local.properties` in the root directory, like the following:

```
API_KEY=YOUR_API_KEY_GOES_HERE
```

## Project Structure

### Approach

I followed the `api/impl` structure for the modules. Each module has an `api` module and an `impl` module. 
The `api`module provides the contracts and data models that the module exposes to other modules. The `impl` module provides the implementation of the contracts and business logic. 
If a module needs to use or access another module's api, it should depend on the `api` module instead of the `impl` module.

This approach has multiple benefits:
- It avoids cyclic dependencies between modules.
- It improves the build performance by only building the necessary modules.
- It flattens the module dependency hierarchy, ultimately reducing the complexity of the build process and improving
  build performance.

### Modules

- `app`: The main Android application module
- `build-logic`: The build logic for the project. This includes plugins and configurations for the project. This module is the place for all common build logic configuration for the project. It is used by all modules in the project and provides a common configuration for all build types. The build logic plugins are used to configure the build process. Currently, the following plugins are used:
    - `arcyto.android.app`: The plugin providing common configuration for the Android application (app module)
    - `arcyto.android.library`: The plugin providing common configuration for the Android libraries (`core` and`feature` modules).
    - `arcyto.anvil.library`: The plugin providing common configuration for the libraries using Anvil for dependency injection.
- `core`: The core libraries providing the core functionality.
    - `core:data:local`: The module providing local data storage using DataStore and local resources.
    - `core:data:remote`: The module providing remote data access using Retrofit and OkHttp. This module provides a common interface for accessing remote data and a default implementation using Retrofit and OkHttp. The default implementation will be used if no other implementation is provided.
    - `core:di`: The module providing dependency injection using Dagger and Anvil. This module provides the standard Android components for injection and adds custom components for the application (e.g. `AppComponent` for the application itself). The module also provides a set of utilities for injecting common types (e.g.
      `@Inject val context: Context`).
    - `core:design-system`: The module providing the common design system for the application. The module provides the standard Android components for the design system (e.g. `MaterialTheme`, `LightTheme`, `DarkTheme`, `Typography`, etc.) and customized composable components to be used in the feature modules.
    - `core:formatter`: The module providing the common formatter for the application, including price and date formatting.
    - `core:tooling`: The module providing tools for the application.
        - `core:tooling:extension`: The module providing common extension functions for the application.
        - `core:tooling:test`: The module providing tools for testing.
- `feature`: The feature modules providing the app features.
    - `feature:details`: The feature module displaying the details of a coin.
        - `feature:details:api`: The feature module providing the API for the details feature.
        - `feature:details:impl`: The feature module displaying the details of a coin, including the current price, market cap, and total volume. It also displays the prices in three currencies (USD, EUR, and GBP).
    - `feature:history`
        - `feature:history:api`: The feature module providing the API for the history feature.
        - `feature:history:impl`: The feature module fetching the historical price data from the API and displaying them in a list. I also fetches the latest price data every 6 seconds from the API and displays it as the first item in the list.

### Workflows

I used Github Actions as the CI of the project. The following workflows are defined:

- `Build & Test`: The workflow for building the app and running the unit tests. It triggers on every push and pull requests to the main branch.