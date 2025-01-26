# Arcyto

## Summary

This project is a demo application created for show casing my skills in Android development.
It is a straightforward Android app that fetches the historical price data from the Coingecko API and displays it in a
list.

I aimed to showcase what a release-ready project looks like, using a modular structure with MVVM and clean architecture.
Below is a brief explanation of each module.

## Modules

- `app`: The main Android application
- `build-logic`: The build logic for the project. This includes plugins and configurations for the project. This module
  is the place for all common build logic configuration for the project. It is used by all modules in the project and
  provides a common configuration for all build types. The build logic plugins are used to configure the build process.
- `core`: The core libraries providing the core functionality.
    - `core:data:local`: The module providing local data storage using DataStore.
    - `core:data:remote`: The module providing remote data access using Retrofit and OkHttp. This module provides a
      common interface for accessing remote data and a default implementation using Retrofit and OkHttp. The default
      implementation will be used if no other implementation is provided.
    - `core:di`: The module providing dependency injection using Dagger and Anvil. This module provides the standard
      Android components for injection and adds custom components for the application (e.g. `AppComponent` for the
      application itself). The module also provides a set of utilities for injecting common types (e.g.
      `@Inject val context: Context`).
    - `core:design-system`: The module providing the common design system for the application. The module provides the
      standard Android components for the design system (e.g. `MaterialTheme`, `Typography`, etc.).
      The module also provides a set of utilities for styling the application (e.g. `ArcytoTheme` for the default
      application theme).
    - `core:location`: The module providing location services using Mock Location Data Source and Google Play Services.
    - `core:tooling`: The module providing tools for the application.
        - `core:tooling:extension`: The module providing extensions for the application.
        - `core:tooling:test`: The module providing tools for testing.
- `feature`: The module providing the app features.
    - `feature:history`: The feature module fetching the historical price data from the API and displaying them in a
      list.