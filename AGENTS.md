# BluetoothTerminalApp

This document provides an immediate, high-level understanding of the BluetoothTerminalApp codebase
for AI assistants, agents, and new developers.

## 1. Architecture Overview

The project follows **Clean Architecture** principles combined with **MVVM (
Model-ViewModel-ViewModel)** for the presentation layer. It is a single-module Android application (
`app` module) with internal layering and feature-based organization.

### Directory Structure

- `app/src/main/java/com/eva/bluetoothterminalapp/`
    - `data/`: Implementations of domain repositories. Contains Android-specific Bluetooth and BLE
      logic, DataStore persistence (Protobuf), and mappers.
    - `domain/`: Business logic abstractions. Contains repository interfaces, domain models, enums,
      and exceptions. This layer is independent of Android-specific implementation details where
      possible.
    - `di/`: Koin dependency injection modules (organized by functionality like `BLEModule`,
      `BTClassicModule`, etc.).
    - `presentation/`: UI logic and State management.
        - Organized by features: `feature_connect`, `feature_devices`, `feature_le_connect`, etc.
        - Each feature contains its own ViewModels, event contracts, and screen-level composables.
    - `ui/`: Global UI theme (Color, Typography, Shape).

## 2. Core Tech Stack & Dependencies

- **UI:** Jetpack Compose (Material 3) with **Compose Destinations** for navigation.
- **Concurrency:** Kotlin Coroutines & Flow for asynchronous operations and reactive state.
- **Dependency Injection:** **Koin** (using `KoinStartup` for initialization).
- **Persistence:** **DataStore** with **Protobuf** serialization for app settings.
- **Bluetooth:** Standard Android Bluetooth APIs for Classic BT and Bluetooth Low Energy (BLE).
- **Serialization:** Kotlinx Serialization for JSON and Protobuf for DataStore.

## 3. Key Logic Hubs (The "Where to Look" Guide)

If you need to modify or understand core functionality, start here:

### Bluetooth Classic (BT)

- **Scanning:** `data/bluetooth/AndroidBluetoothScanner.kt`
- **Connection:** `data/bluetooth/AndroidBTClientConnector.kt` and `AndroidBTServerConnector.kt`
- **Data Transfer:** `data/bluetooth/BluetoothTransferService.kt`

### Bluetooth Low Energy (BLE)

- **Scanning:** `data/bluetooth_le/AndroidBluetoothLEScanner.kt`
- **Connection & GATT:** `data/bluetooth_le/AndroidBLEClientConnector.kt` and
  `data/bluetooth_le/BLEClientGattCallback.kt`

### Settings & State

- **Persistence:** `data/datastore/` (Implementation) and `domain/settings/repository/`
  (Interfaces).
- **Global Settings ViewModel:** `presentation/feature_settings/AppSettingsViewModel.kt`

### Navigation

- **Graph Definition:** `presentation/navigation/AppNavigation.kt`
- Uses **Compose Destinations** (look for `@Destination` annotations on composables).

## 4. Data Flow & State Management

The app follows a unidirectional data flow (UDF):

1. **User Action:** UI triggers an `Event` (e.g., `BTSettingsEvent`) in the `ViewModel`.
2. **ViewModel logic:** The `ViewModel` performs logic or calls a `Repository` method.
3. **Repository Action:** The `Repository` (implemented in `data/`) interacts with the Bluetooth
   hardware or `DataStore`.
4. **State Update:** `DataStore` or Bluetooth status flows back as a `Flow`.
5. **UI Observation:** The `ViewModel` converts the `Flow` into a `StateFlow` (often using
   `stateIn`). The Compose UI observes this state and recomposes.

## 5. AI/Agent Context & Guidelines

- **UI Development:** Always use **Jetpack Compose**. Follow the Material 3 design system. Design
  tokens should be pulled from the `ui/theme` package.
- **Navigation:** Use **Compose Destinations**. Do not manually manage the NavGraph; use the
  generated code and annotations.
- **Dependency Injection:** Use **Koin**. When adding new services or ViewModels, ensure they are
  registered in the appropriate module in the `di/` package.
- **Immutability:** State exposed from ViewModels should be immutable. Use
  `kotlinx-collections-immutable` where appropriate.
- **Permissions:** Bluetooth operations require runtime permissions. Ensure you check for
  `android.permission.BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN`, and `ACCESS_FINE_LOCATION` where
  necessary.
- **Architecture Integrity:** Do not call Android Bluetooth APIs directly from ViewModels. Always go
  through a domain-defined interface (Repository) and provide an implementation in the `data/`
  layer.
- **Naming Conventions:** Repository implementations should be prefixed with `Android` if they are
  platform-specific (e.g., `AndroidBluetoothScanner`).

## 6. Performance & Optimization (R8)

- **R8 Full Mode:** The project is configured to use **strict R8 Full Mode** (no compatibility
  flags). This allows for aggressive constructor and member shrinking.
- **Rule Discipline:** Avoid adding broad keep rules (e.g., `-keep class com.package.** { *; }`).
  Prefer narrow rules or relying on library consumer rules.
- **Protobuf:** Unused field shrinking for Protobuf is enabled via `-shrinkunusedprotofields` in
  `proguard-rules.pro`.
- **Validation:** When upgrading libraries or adding reflection-heavy code, always verify
  functionality in a `release` build variant to ensure R8 hasn't stripped required members.

