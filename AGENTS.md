# AGENTS.md

This file provides instructions and context for AI coding agents working on the GitHubApp project.

## Project Overview

GitHubApp is an Android application for browsing GitHub repositories, built with Kotlin and Jetpack Compose following clean architecture and multi-module design principles.

- **Package name:** `com.matijasokol.githubapp`
- **Min SDK:** 24 | **Target/Compile SDK:** 37
- **Kotlin:** 2.4.10 | **AGP:** 9.3.1
- **Product flavors:** `free`, `paid`

## Architecture

The project follows a **multi-module architecture** with clear separation of concerns:

```
app/                     → Application module (entry point, DI setup, navigation)
├── core/                → Shared utilities, base classes, error types (pure Kotlin/JVM)
├── core-ui/             → Reusable Compose UI components, theming, navigation helpers
├── repo/                → Feature: GitHub repositories
│   ├── domain/          → Business logic, models, use cases (pure Kotlin/JVM)
│   ├── datasource/      → Data layer (Ktor network + SQLDelight local database)
│   ├── datasource-test/ → Test doubles for the data layer
│   ├── list/            → Repository list screen (UI + ViewModel)
│   └── detail/          → Repository detail screen (UI + ViewModel)
├── konsist/             → Project-wide architecture and naming rules (Konsist tests)
├── test/                → Shared test fixtures
└── build-logic/         → Gradle convention plugins
```

### Module Dependency Rules

- **domain** modules must NOT depend on Android, data, or UI layers.
- **datasource** depends on **domain** (implements interfaces defined there).
- **UI/feature** modules (list, detail) depend on **domain** and **core-ui**.
- **app** module wires everything together with Hilt DI.
- Use `projects.*` typesafe accessors for inter-module dependencies (e.g., `projects.repo.domain`).

## Tech Stack

| Layer           | Technology |
|-----------------|---|
| Language        | Kotlin 2.4.10 with Coroutines + Flow |
| UI              | Jetpack Compose with Material 3 |
| Navigation      | Navigation 3 with Shared Element Transitions |
| Networking      | Ktor + Kotlinx Serialization |
| Local Database  | SQLDelight |
| Background Work | Coroutines + Flow |
| Image Loading   | Coil 3 |
| DI              | Hilt |
| Error Handling  | Arrow |
| Testing         | JUnit 6, MockK, Turbine, Kluent |
| Quality         | Ktlint, Detekt, Konsist architecture tests |
| Build           | Gradle convention plugins + Version Catalog (`gradle/libs.versions.toml`) |

## Code Conventions

### General

- Use Kotlin idioms: `data class`, `sealed interface`, extension functions, `when` expressions.
- Prefer immutable data (`val`, `ImmutableList` from kotlinx-collections-immutable).
- Use `@Stable` annotation on Compose state classes.
- Do NOT use `var` in state classes; use `MutableStateFlow` + `.update {}` in ViewModels.
- Keep functions small and single-purpose.

### Presentation Layer (MVI Pattern)

Each feature screen follows a strict **MVI** (Model-View-Intent) pattern:

- **`*State`** — `@Stable data class` holding all UI state, using `ImmutableList` for collections.
- **`*Event`** — `sealed interface` representing user intents/interactions sent TO the ViewModel.
- **`*Action`** — `sealed interface` representing one-shot actions sent FROM the ViewModel to the UI (navigation, messages). Delivered via `Channel`.
- **`*ViewModel`** — `@HiltViewModel` class exposing:
  - `val state: StateFlow<*State>` (combined from multiple flows using `combine`)
  - `val actions: Flow<*Action>` (from `Channel.receiveAsFlow()`)
  - `fun onEvent(event: *Event)` as the single entry point for UI interactions.
- **`*UiMapper`** — Separate class to map domain state to UI state (injected into ViewModel).

### Domain Layer

- Use cases are classes with `operator fun invoke(...)` (injectable via `@Inject constructor`).
- Domain models are plain `data class` types with no framework annotations.
- Interfaces (`RepoService`, `RepoCache`) define contracts implemented in the datasource layer.

### Data Layer

- Network calls use `safeNetworkCall {}` wrapper returning `Either<NetworkError, T>` (Arrow).
- DTOs use `@Serializable` (Kotlinx Serialization) and are mapped to domain models via mapper functions.
- Database operations use SQLDelight-generated APIs.

### Dependency Injection

- Use Hilt `@Module` / `@Provides` / `@Binds` for wiring.
- ViewModels use `@HiltViewModel` with `@Inject constructor`.
- Use cases and mappers use `@Inject constructor` directly (no module needed).

### Build System

- All build configuration goes through **convention plugins** in `build-logic/convention/`.
- Dependency versions live in `gradle/libs.versions.toml`. Never hardcode versions in `build.gradle.kts`.
- Use custom plugin aliases: `githubapp.android.library`, `githubapp.android.library.compose`, `githubapp.jvm.library`, etc.

## Testing Guidelines

- Unit tests use **JUnit 6** (`@Test` from `org.junit.jupiter.api`).
- Use **Turbine** for testing `StateFlow`/`Flow` emissions (`flow.test { awaitItem() }`).
- Use **Kluent** assertion style (e.g., `` value `should be` expected ``, `list.shouldNotBeEmpty()`).
- Use **MockK** for mocking dependencies when needed.
- Use **fakes** (preferred over mocks) for data layer tests (`RepoServiceFake`, `FakePaginator`).
- Coroutine tests use `runTest` with a custom `AndroidCoroutinesExtension` (JUnit 6 extension).
- Compose UI tests use `compose-junit4` with test tags defined in `**/test/TestTags.kt`.
- Konsist architecture tests live in `konsist/src/test/kotlin/com/matijasokol/githubapp/konsist` and run with `./gradlew konsist:test`. They enforce package boundaries, MVI/ViewModel conventions, immutable UI state collections, DTO naming/serialization, Compose placement, and related project structure rules.
- Test file naming: `<ClassUnderTest>Test.kt`.
- Test method naming: backtick-style descriptive names (e.g., `` `should RETURN SUCCESS STATE when request was successful`() ``).

## Do's and Don'ts

### Do

- ✅ Follow existing MVI pattern for new screens (State, Event, Action, ViewModel, UiMapper).
- ✅ Place new features in their own module under the appropriate feature folder.
- ✅ Use `Either` from Arrow for error handling in network/data operations.
- ✅ Use convention plugins for module setup instead of duplicating build logic.
- ✅ Use `ImmutableList` for list properties in state classes.
- ✅ Write unit tests for ViewModels and use cases.
- ✅ Use `combine` to derive state from multiple flows.
- ✅ Run `./gradlew konsist:test`, `./gradlew detekt`, and `./gradlew ktlintCheck` before submitting changes.

### Don't

- ❌ Don't add Android framework dependencies to `domain` or `core` modules.
- ❌ Don't hardcode strings in UI — use `stringResource` in composables and the `Dictionary` abstraction in ViewModels or mappers.
- ❌ Don't use `LiveData` — use `StateFlow` and `Channel` exclusively.
- ❌ Don't use `mutableStateOf` in ViewModels — use `MutableStateFlow`.
- ❌ Don't put business logic in Composables or ViewModels — extract to use cases.
- ❌ Don't add dependencies directly; add them to `libs.versions.toml` first.
- ❌ Don't use `GlobalScope` or unstructured coroutines.
- ❌ Don't use wildcard imports.

## Useful Commands

```bash
# Build the debug project
./gradlew assembleDebug

# Build the release project
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run Konsist architecture and naming tests
./gradlew konsist:test

# Run detekt static analysis
./gradlew detekt

# Run ktlint check
./gradlew ktlintCheck

# Run ktlint format
./gradlew ktlintFormat
```
