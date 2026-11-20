# BluetoothTerminalApp

Single-module Android app (`com.eva.bluetoothterminalapp`). Clean Architecture + MVVM. Layers:
`data/` (Android impls) → `domain/` (interfaces/models) ← `presentation/` (UI/state), wired by
`di/` (Koin). Presentation depends on `domain/` only, never on concrete `data/` classes.

## Structure (`app/src/main/java/com/eva/bluetoothterminalapp/`)

- `data/`: `bluetooth/` (classic: scanner, client/server connectors, transfer service,
  `receivers/`, `util/`), `bluetooth_le/` (BLE client + GATT callback), `ble_server/` (GATT
  peripheral server + `BLEConstructorDSL`), `services/` (SPP automation), `datastore/` (Protobuf
  settings), `device/` (battery/light sensors), `mapper/`, `samples/`, `utils/`.
- `domain/`: `bluetooth/`, `bluetooth_le/`, `device/`, `settings/`, `exceptions/`, `utils/`
  (interfaces, enums, models).
- `di/`: Koin modules (`AppModule` + `KoinDependencyContainer`).
- `presentation/`: `feature_devices/`, `feature_connect/` (`bt_client`, `bt_server`,
  `bt_profile`), `feature_le_connect/`, `feature_le_server/`, `feature_settings/`, `navigation/`,
  `composables/`, `contracts/`, `util/`.
- `ui/theme/`: theme tokens.

## Tech stack

Versions: `gradle/libs.versions.toml`. Build: `app/build.gradle.kts`.

- AGP 9.5.1, Gradle 9.9.1, Kotlin 2.4.21, JDK 17; compileSdk/targetSdk 37, minSdk 26.
- Jetpack Compose + **Material 3 Expressive** + **Material Kolor** dynamic theming.
- **Navigation 3** (serializable `NavKey` back stack) + adaptive list-detail.
- **Koin 4.2.2** with the Compiler Plugin + annotations, started via `KoinStartup`.
- DataStore + Protobuf (`app/src/main/proto/`); kotlinx Serialization.
- Android Bluetooth APIs: Classic SPP + BLE (client and GATT server).
- Tests: JUnit 4, `kotlin-test`, `assertk`, `kotlinx-coroutines-test`.

## Key files

- **BT Classic:** `data/bluetooth/AndroidBluetoothScanner.kt`, `AndroidBTClientConnector.kt`,
  `AndroidBTServerConnector.kt`, `BluetoothTransferService.kt`. SPP automation:
  `data/services/` (see `docs/spp-connection.md`).
- **BLE:** `data/bluetooth_le/AndroidBluetoothLEScanner.kt`, `AndroidBLEClientConnector.kt`,
  `BLEClientGattCallback.kt`; server in `data/ble_server/`.
- **Settings:** `data/datastore/` + `domain/settings/repository/`; schemas in `app/src/main/proto/`.
- **Navigation:** `presentation/navigation/` (`AppNavigation.kt`, `NavRoutes.kt`,
  `NavDestinationBuilder.kt`, `screens/`).
- **DI:** `di/AppModule.kt` + feature modules.

## Navigation 3

- Routes are `@Serializable` members of the `NavRoutes` sealed interface (implements `NavKey`) in
  `NavRoutes.kt`.
- Each screen implements `NavDestinationBuilder`, annotated
  `@Single(binds = [NavDestinationBuilder::class])`, and registers itself via
  `entry<NavRoutes.X> { ... }`.
- `AppNavigation` collects all builders and drives a `NavDisplay` with Dialog / BottomSheet /
  list-detail scene strategies.
- Navigate with plain list ops: `backStack.add(NavRoutes.X)` / `removeLastOrNull()`.
- **Add a screen:** add a route to `NavRoutes` and a `NavDestinationBuilder` impl (auto-scanned).
  No manual graph wiring.

## Dependency injection (Koin)

- `AppModule` is `@Module(includes = [...])`; `KoinDependencyContainer` is `@KoinApplication`;
  started in `BluetoothTerminalApp` via `KoinStartup`.
- Use `@Single` / `@Factory` / `@KoinViewModel`; runtime args via `@InjectedParam` +
  `parametersOf(...)`. `PresentationModule` `@ComponentScan`s the `presentation` package.
- Get ViewModels in Compose with `koinViewModel()`.

## Data flow (UDF)

UI event → ViewModel (`AppViewModel`) → domain Repository → `Flow` → `StateFlow` (`stateIn`) →
`collectAsStateWithLifecycle()`. One-off effects (snackbar/toast/navigate-back) go through
`uiEvents: SharedFlow<UiEvents>` handled by `UIEventsSideEffect`.

## Guidelines

- Compose + Material 3 Expressive; pull tokens from `ui/theme`, never hardcode colors.
- Navigation 3 only — no `NavController`/`NavHost`/Compose Destinations.
- Register new bindings with Koin annotations + Compiler Plugin.
- Keep ViewModel state immutable (`kotlinx-collections-immutable`).
- Permissions: `BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN`, `BLUETOOTH_ADVERTISE`,
  `ACCESS_FINE_LOCATION` (BLE scan).
- Never call Android Bluetooth APIs from ViewModels; go through a `domain/` interface.
- Platform-specific impls are prefixed `Android` (e.g., `AndroidBluetoothScanner`).
- Inject `PlatformCoroutineDispatchers`; don't reference `Dispatchers` directly.
- Tests live in `app/src/test/`; use fakes, not Android mocks.

## Build & R8

- `./gradlew assembleDebug` | `testDebugUnitTest` | `connectedDebugAndroidTest`. CI runs
  `assembleDebug` on PRs to `main`.
- Release signing via `keystore.properties` or `STORE_*` / `KEY_*` env vars.
- R8 full mode: avoid broad keep rules; Protobuf keeps in `app/src/main/keepRules/rules.keep`.
- `stability_config.conf` marks types stable for the Compose compiler.
- Verify a `release` build when upgrading libraries or adding reflection-heavy code.

## Docs

`docs/spp-connection.md`, `README.md`, `CONTRIBUTING.md`.
