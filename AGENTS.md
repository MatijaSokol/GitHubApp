# AGENTS.md

This file provides instructions and context for AI coding agents working on the GitHubApp project.

See [README.md](README.md) for the feature overview, build variants, CI workflows, signing, versioning, and API/data notes. This file holds the rules agents must follow. When a change affects both, update both.

## Project Overview

GitHubApp is an Android application for browsing GitHub repositories, built with Kotlin and Jetpack Compose following clean architecture and multi-module design principles.

- **Package name:** `com.matijasokol.githubapp`
- **Min SDK:** 24 | **Target/Compile SDK:** 37
- **Versions:** see `gradle/libs.versions.toml` (source of truth; bumped by Dependabot). Uses AGP 9 APIs (no `CommonExtension`).
- **Product flavors:** `environment` (`dev`, `prod`) × `mode` (`free`, `paid`) → variants such as `devPaidDebug`, `prodFreeRelease`. `free` adds the `.free` applicationId suffix and blocks the detail screen.

## Architecture

The project follows a **multi-module architecture** with clear separation of concerns:

```text
GitHubApp/
├── app/                 → Application module (entry point, app-level DI setup, navigation)
├── core/                → Shared utilities, error types, sort order, app mode, logging contract, build config model (pure Kotlin/JVM)
├── core-ui/             → Reusable Compose UI components, theming, navigation helpers
├── repo/                → Empty container project for the repository feature (`:repo`, no build file of its own)
│   ├── domain/          → Business logic, models, use cases (pure Kotlin/JVM)
│   ├── datasource/      → Data layer (Ktor network + SQLDelight local database, pure Kotlin/JVM)
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
- **app** module owns application-level Hilt setup, Android-specific bindings, and composition decisions, while feature/data modules own DI bindings for implementations they define when the binding fits that module's platform and component scope.
- Use `projects.*` typesafe accessors for inter-module dependencies (e.g., `projects.repo.domain`).
- The README's [Module Dependency Graph](README.md#module-dependency-graph) shows the current production dependencies and matches the Konsist rule `package layers have correct dependencies`. When adding or removing a module or an inter-module dependency, update both the graph and the Konsist rule in the same change.
- The README's [Gradle Configuration](README.md#gradle-configuration) section describes the convention plugins and how they compose. Update it when adding, removing, or changing what a convention plugin applies.

## Tech Stack

| Layer           | Technology                                                                |
|-----------------|---------------------------------------------------------------------------|
| Language        | Kotlin with Coroutines + Flow                                             |
| UI              | Jetpack Compose with Material 3 and Backdrop                              |
| Navigation      | Navigation 3 with Shared Element Transitions                              |
| Networking      | Ktor + Kotlinx Serialization                                              |
| Local Database  | SQLDelight                                                                |
| Background Work | Coroutines + Flow                                                         |
| Image Loading   | Coil 3                                                                    |
| DI              | Hilt                                                                      |
| Error Handling  | Arrow                                                                     |
| Logging         | `AppLogger` abstraction backed by Timber (debug builds only)              |
| Testing         | JUnit 6, MockK, Turbine, Kluent                                           |
| Quality         | Ktlint, Detekt, Android Lint, XML format, Konsist architecture tests      |
| Build           | Gradle convention plugins + Version Catalog (`gradle/libs.versions.toml`) |

## Code Conventions

### General

- Use Kotlin idioms: `data class`, `sealed interface`, extension functions, `when` expressions.
- Prefer immutable data (`val`, `ImmutableList` from kotlinx-collections-immutable).
- Let the Compose compiler infer stability. Add `@Stable` only when a state type contains fields Compose treats as unstable and the stability contract is valid; do not annotate immutable text or value models unnecessarily.
- Do NOT use `var` in state classes; use `MutableStateFlow` + `.update {}` in ViewModels.
- Keep functions small and single-purpose.
- Code style is defined in `.editorconfig` (ktlint) and `quality/detekt.yml` (detekt + Compose rules). Key rules: 4-space indent, max line length **120** (detekt allows 150 — follow 120), trailing commas on declarations and call sites, a blank line after the class header, and multiline class signatures when there are 2+ parameters.
- Run `./gradlew ktlintFormat` to auto-fix style before `./gradlew ktlintCheck detekt`.
- XML files are checked by `./gradlew xmlFormatCheck` (`build-logic/convention/src/main/kotlin/quality/`): indentation width must be a multiple of 4 (spaces or tabs; a tab counts as 4 spaces), no trailing whitespace, and a newline at end of file. Text inside multi-line `<!-- -->` comments is not indentation-checked.

### Presentation Layer (MVI Pattern)

Each feature screen follows a strict **MVI** (Model-View-Intent) pattern:

- **`*State`** — Immutable UI state: either a single `data class` (e.g. `RepoListState`) or a `sealed interface` of data-class variants when the screen has mutually exclusive modes such as Loading/Success/Error (e.g. `RepoDetailState`). Use `ImmutableList` for collections. Use `@Stable` only when stability cannot be inferred safely.
- **`*Event`** — `sealed interface` representing user intents/interactions sent TO the ViewModel.
- **`*Action`** — `sealed interface` representing one-shot actions sent FROM the ViewModel to the UI (navigation, messages). Delivered via `Channel`.
- **`*ViewModel`** — `@HiltViewModel` class (plain `@Inject constructor`, or `@AssistedInject` when it needs a navigation destination — see Dependency Injection) exposing:
  - `val state: StateFlow<*State>` (combined from multiple flows using `combine`)
  - `val actions: Flow<*Action>` (from `Channel.receiveAsFlow()`)
  - `fun onEvent(event: *Event)` as the single entry point for UI interactions.
- **`*UiMapper`** — Separate class to map domain state to UI state, retaining resource-backed text as unresolved `UiText` (injected into ViewModel). Expose one named function per message or action the ViewModel emits (e.g. `RepoListUiMapper.profileBrowserErrorMessage()`, `RepoDetailsUiMapper.repoBrowserErrorAction()`); never a generic `toAction(event)` that has to throw for events that produce no action.
- Screen composables resolve `UiText` at the UI boundary. Child composables needing up to three text values should receive individual resolved strings; when more than three text arguments are required, pass the relevant text state/model directly. Do not pass the entire screen state.

### Compose Previews

- Wrap previews in `GitHubAppPreviewContent` from `core-ui` so they receive the app theme and required composition locals. It renders `GitHubAppTheme(dynamicColor = false)`, so previews always show the static brand palettes.
- Use a direct `@Preview` with a `PreviewParameterProvider` for screen state variants. Use the shared `GitHubAppDevicePreviews`, `GitHubAppThemePreviews`, and `GitHubAppLargeFontPreview` annotations for focused configuration checks; do not create a Cartesian product of states and configurations.
- Keep preview functions private and suffix their names with `Preview`.
- Keep deterministic preview fixtures in the owning feature. Do not initialize ViewModels, navigation, network access, or runtime services from previews.

### Theming

- `GitHubAppTheme` (`core-ui/.../theme/Theme.kt`) supports Material You dynamic color: with `dynamicColor = true` (the default) on Android 12+ (API 31) it uses `dynamicLightColorScheme` / `dynamicDarkColorScheme`; otherwise it uses the static `LightColorPalette` / `DarkColorPalette` built from `Color.kt`. Dark mode follows `isSystemInDarkTheme()`. Keep all theme logic in `Theme.kt`; `minSdk` stays 24, so use a runtime `Build.VERSION.SDK_INT` check.
- Style UI with `MaterialTheme.colorScheme` roles, never hard-coded `Color(0x...)` values outside `core-ui/theme` (where `Color.kt` defines the static palettes and shimmer constants), so it follows the wallpaper. Colors that are not part of `ColorScheme` are provided by the theme (e.g. `LocalShimmerColors` for `Modifier.shimmerEffect`) with a static and a dynamic variant.
- Compose UI and end-to-end tests render inside `GitHubAppTheme(dynamicColor = false)` so results don't depend on the emulator's wallpaper.

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
- Place DI bindings in the module that owns the implementation when the choice is not app-specific and the target Hilt component is available to that module.
- Keep bindings in `app` when the application composes or selects between implementations, such as flavor-specific, fake-vs-real, Android application setup, Android context providers, or Android-specific Hilt components.
- ViewModels without navigation arguments use `@HiltViewModel` + `@Inject constructor`.
- ViewModels that need a Navigation 3 destination use `@HiltViewModel(assistedFactory = X.Factory::class)` + `@AssistedInject constructor(@Assisted destination: Destination.Foo, ...)` with a nested `@AssistedFactory interface Factory`, and are obtained in the nav entry via `hiltViewModel<X, X.Factory>(creationCallback = { it.create(key) })` (see `RepoDetailViewModel`). Do not use `SavedStateHandle` for navigation arguments.
- Use cases and mappers use `@Inject constructor` directly (no module needed).
- Build information (`isDebug`, `applicationId`, `buildType`, `flavor`, `versionName`, `versionCode`) is exposed as the `AppBuildConfig` data class in `core` (`com.matijasokol.core.buildconfig`), so pure Kotlin/JVM modules without a generated `BuildConfig` can use it too. Only `app` creates it, in `di/BuildConfigModule` from its generated `BuildConfig`. Inject `AppBuildConfig` instead of reading `BuildConfig` anywhere else.

### App Modes (free / paid)

- Mode-specific code lives in flavor source sets: `app/src/free/java` and `app/src/paid/java`. Each defines its own `ModeChecker` exposing `AppMode`. Keep class names and signatures identical in both.
- Mode gating belongs in `app` (`CanShowDetails` → `NavigatorImpl`, which returns `NavigationError.DetailsUnavailable` for `RepoDetail` in free mode). Feature modules stay mode-agnostic.
- Mode-specific tests go in `app/src/testFree` and `app/src/testPaid`. Update both when mode behavior changes, and verify both flavors (e.g. `./gradlew testDevFreeDebugUnitTest testDevPaidDebugUnitTest`).

### Build System

- All build configuration goes through **convention plugins** in `build-logic/convention/`.
- Dependency versions live in `gradle/libs.versions.toml`. Never hardcode versions in `build.gradle.kts`.
- Apply convention plugins through their version-catalog aliases, e.g. `alias(libs.plugins.githubapp.library)`, `alias(libs.plugins.githubapp.library.compose)`, `alias(libs.plugins.githubapp.jvm.library)` (plugin IDs `githubapp.android.library`, `githubapp.android.library.compose`, `githubapp.jvm.library`). Each module's `plugins { }` block shows which convention plugins it applies.
- The Easylauncher plugin is added by the `githubapp.easylauncher` convention plugin (`EasyLauncherConventionPlugin`, applied by `githubapp.android.application`). Debug variants get a bottom ribbon on the launcher icon labelled with the flavor name (`devFree`, `prodPaid`, ...), and release build types are explicitly disabled. Ribbon colors and sizes are private top-level constants in `EasyLauncherConventionPlugin.kt`. Icons are badged at build time into generated resources, so never commit badged icons or edit the source artwork for this.

## Testing Guidelines

- Unit tests use **JUnit 6** (`@Test` from `org.junit.jupiter.api`).
- Use **Turbine** for testing `StateFlow`/`Flow` emissions (`flow.test { awaitItem() }`).
- Use **Kluent** assertion style (e.g., `` value `should be` expected ``, `list.shouldNotBeEmpty()`).
- Use **MockK** for mocking dependencies when needed.
- Use **fakes** (preferred over mocks) for data layer tests (`RepoServiceFake`, `RepoCacheFake`).
- Coroutine/ViewModel tests use `runTest` with `@ExtendWith(MainDispatcherExtension::class)` from `com.matijasokol.test.coroutines` (the `test` module's test fixtures, consumed via `testImplementation(testFixtures(projects.test))`). It replaces `Dispatchers.Main` with a `StandardTestDispatcher` for each test. Reuse it instead of creating per-module copies.
- Instrumented / Compose UI tests (`src/androidTest`) use **JUnit 4** (`org.junit.Test`, `org.junit.Rule`), not Jupiter, with `createComposeRule()` and test tags defined in `**/test/TestTags.kt`.
- App-level end-to-end tests use `@HiltAndroidTest` + `HiltAndroidRule` (rule order 0), replace production modules with `@UninstallModules(...)` and a nested test `@Module` that provides fakes from `repo:datasource-test`, and run with `CustomTestRunner` (see `RepoListEndToEnd`).
- Konsist architecture tests live in `konsist/src/test/kotlin/com/matijasokol/githubapp/konsist` and run with `./gradlew konsist:test`. They enforce:
  - Package layer dependencies; `domain` free of Android, datasource, and UI packages; datasource free of UI packages.
  - Datasource implementations (e.g. `RepoServiceImpl`, `RepoCacheImpl`) implement their matching domain contract; DTOs use the `Dto` suffix and `@Serializable`.
  - Use cases use the `UseCase` suffix and expose `operator fun invoke` as their single public declaration; constructor parameters are named after their type (e.g. `repoService: RepoService`).
  - ViewModels use the `ViewModel` suffix and `@HiltViewModel`, have a single constructor, do not depend on `Navigator`, expose `state: StateFlow` and `actions: Flow`, and have `onEvent` as the only public entry point. Constructor parameters are named after their type (e.g. `sortReposUseCase: SortReposUseCase`); Hilt `@Assisted` parameters are exempt.
  - Every ViewModel (with declarations) and use case has a unit test class named `<ClassUnderTest>Test` that holds the instance under test in a class-level `sut` property (e.g. `private lateinit var sut: RepoListViewModel`).
  - Every feature package with a ViewModel declares `*State`, `*Event`, `*Action`, and `*UiMapper`; Events and Actions are sealed.
  - State and UI models use `ImmutableList` for exposed collections; data classes use only `val` properties.
  - `@Composable` functions live only in UI modules; string resources are resolved only through `UiText`; ViewModels and mappers do not touch Android resources.
  - Package names are lowercase and match file paths; no wildcard imports.
  - No `m`-prefixed fields (`mFoo`), no `android.util.Log` imports, Timber imports only in `app`'s logging setup (`com/matijasokol/githubapp/logging/`), and no empty `.kt` files. Both logging rules fail with a message pointing to `AppLogger`.
  - Unit-test source sets (`src/test`, `src/testFree`, `src/testPaid`, `src/testFixtures`) use only JUnit 6 `org.junit.jupiter.api.Test`, never JUnit 4 `org.junit.Test`; instrumented `androidTest` is exempt.
- `domain` tests build their inputs from domain models only and must not depend on `repo:datasource` or `repo:datasource-test`. Tests for datasource implementations (e.g. `BasicPaginatorTest`) live in `repo/datasource/src/test`.
- Test file naming: `<ClassUnderTest>Test.kt`.
- Test method naming: backtick-style descriptive names (e.g., `` `should RETURN SUCCESS STATE when request was successful`() ``).

## Debugging / Logging

- Log through `com.matijasokol.core.logging.AppLogger` in every module, Android and JVM alike: inject it and call `appLogger.d(message)`, `appLogger.e(message, throwable, tag = LogTag.NETWORK)`, etc. It lives in `core`, so pure Kotlin/JVM modules (`repo:domain`, `repo:datasource`, ...) can use it even though Timber is Android-only.
- Don't call Timber directly outside `app`'s logging setup (`githubapp/logging/`, enforced by Konsist), and don't use `android.util.Log` (banned by Konsist).
- `AppLogger` is implemented only in `app` by `TimberLogger` and bound in `di/LoggingModule`. Don't add other production implementations in feature or data modules; test fakes (`AppLoggerFake`) are fine. `RepoListEndToEnd` keeps `LoggingModule` installed.
- `App.onCreate()` calls `initLogging(isDebug = appBuildConfig.isDebug)` (injected `AppBuildConfig`), which plants `Timber.DebugTree()` in debug builds only. Release builds plant no tree, so all logs, including `AppLogger` calls, are no-ops there. Never plant trees outside `app`.
- Tag related logs with the `LogTag` enum in `core` (`com.matijasokol.core.logging.LogTag`); each entry holds its Logcat tag string in `value`. Use `appLogger.d(message, tag = LogTag.NETWORK)` and filter Logcat by that value. Add new tags as enum entries instead of using string literals. An `AppLogger` call without a tag (`null`), or with a tag whose `value` is empty, falls back to the default `AppLogger` tag.
- Unit tests need no logging setup: with no tree planted, logging does nothing. Classes that inject `AppLogger` get an `AppLoggerFake` in tests (fakes over mocks); it records every call as an `Entry` (level, message, throwable, tag), so assert on `entries` or `messages` instead of `verify`. It lives in `repo:datasource-test` (`com.matijasokol.repo.datasourcetest.logging`), next to the other data-layer fakes.
- Network logging: in debug builds the GitHub API client logs every request and response, headers and bodies included, through `AppLogger` under `LogTag.NETWORK`. Filter Logcat with `tag:Network`. `NetworkModule` injects `AppBuildConfig` and `AppLogger`; when `isDebug` is `true` it passes `buildHttpClient` a `networkLogger`, a `NetworkLogger` (the internal Ktor `Logger` that forwards to `AppLogger`), otherwise `null`. Release builds don't install Ktor's `Logging` plugin at all. `Authorization`, `Proxy-Authorization`, `Cookie` and `Set-Cookie` values are logged as `***` (see `network/NetworkLogging.kt`). `networkLogger` defaults to `null`, which installs no logging plugin, so test clients (`RepoServiceFake`) stay silent. Large search responses show up as several consecutive `Network` entries, because Logcat caps an entry at ~4 KB and Timber splits longer messages.
- Inspecting network traffic: debug builds trust user-installed CAs (`app/src/debug/AndroidManifest.xml` + `app/src/debug/res/xml/network_security_config.xml`, user CAs inside `<debug-overrides>` on top of system CAs), so Charles Proxy, Proxyman or mitmproxy can decrypt `api.github.com` and avatar traffic. Install the proxy's root CA on the device/emulator, set the Wi-Fi or emulator proxy, and run a debug variant. Release builds keep default trust and fail TLS through a proxy, which is expected. Keep this config in `src/debug`; never move it to `src/main`, and don't add cleartext traffic or certificate pinning here.

## Do's and Don'ts

### Do

- ✅ Follow existing MVI pattern for new screens (State, Event, Action, ViewModel, UiMapper).
- ✅ Place new features in their own module under the appropriate feature folder.
- ✅ Use `Either` from Arrow for error handling in network/data operations.
- ✅ Use convention plugins for module setup instead of duplicating build logic.
- ✅ Use `ImmutableList` for list properties in state classes.
- ✅ Write unit tests for ViewModels and use cases.
- ✅ Use `combine` to derive state from multiple flows.
- ✅ Run `./gradlew konsist:test`, `./gradlew detekt`, `./gradlew ktlintCheck`, `./gradlew xmlFormatCheck`, and `./gradlew lint` before submitting changes.

### Don't

- ❌ Don't add Android framework dependencies to `domain` or `core` modules.
- ❌ Don't resolve or cache localized strings in mappers or ViewModels, or inject `Context` or `Resources` into them. Use `strings.xml` → `UiText.StringResource` → UI state/action → UI-boundary resolution, including formatted, accessibility, and one-shot message text.
- ❌ Don't call `stringResource` directly in feature composables; `UiText.asString()` is the centralized resolver. Direct `Resources` access is limited to `UiText` and the app-level one-shot message boundary in `AppContent`.
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

# Run unit tests for every module and variant (includes Konsist)
./gradlew test

# Run Konsist architecture and naming tests
./gradlew konsist:test

# Run Android Lint (fails only on issues missing from the module's lint-baseline.xml)
./gradlew lint

# Regenerate Android Lint baselines (only when intentionally accepting existing issues)
./gradlew updateLintBaseline

# Instrumented / Compose UI tests (needs a running device or emulator; not run in CI)
./gradlew app:connectedDevPaidDebugAndroidTest
./gradlew repo:list:connectedDebugAndroidTest repo:detail:connectedDebugAndroidTest

# Run detekt static analysis
./gradlew detekt

# Run ktlint check
./gradlew ktlintCheck

# Run ktlint format
./gradlew ktlintFormat

# Check XML formatting
./gradlew xmlFormatCheck
```

`assembleRelease` and `assemble*Release` need the `GITHUBAPP_STORE_PASSWORD` and `GITHUBAPP_KEY_PASSWORD` environment variables; use debug variants for local verification.

CI (`.github/workflows/pr_checks.yml`) runs `static-analysis`, `build-free`, `build-paid` and `unit-test` on every PR; see the GitHub Actions section in [README.md](README.md) for details. Fix new Android Lint findings instead of adding them to `lint-baseline.xml`.
