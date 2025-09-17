# App Integrity Check

**App Integrity Check** is a simple demo showing how an Android application can verify its integrity at startup. It demonstrates root detection, emulator detection, and basic app integrity checks.

---

## Overview

The integrity check is divided into **three main sections**:

1. **Root Check** – Detects if the device is rooted.  
2. **Emulator Check** – Detects if the app is running on an emulator.  
3. **Application Integrity Check** – Verifies whether the app was installed via a legitimate installer (e.g., Play Store).

> Note: In professional projects, additional third-party validation tools may be used. This demo focuses on the three primary checks.

---

## Architecture

The app runs an **`EnvironmentValidator`** component at startup, which orchestrates all integrity checks:

```
EnvironmentValidator
├── RootChecker
├── EmulatorChecker
└── AppIntegrityChecker
```

- **EnvironmentValidator**: Orchestrates all checks at app launch.  
- **RootChecker**: Performs root detection.  
- **EmulatorChecker**: Detects if the app is running on an emulator.  
- **AppIntegrityChecker**: Checks installation source and basic app integrity.

This structure is modular and can be easily extended with additional checks.

---

## Usage

1. Add `EnvironmentValidator` to your startup flow (e.g., splash screen or main activity).  
2. It will automatically perform all three checks and return a result.  
3. Handle the results appropriately — e.g., show a warning, restrict access, or proceed to the main app.

---

## Example

```kotlin
@Composable
fun StartUp(viewModel: MainViewModel) {
    val state by viewModel.viewState.observeAsState()

    when (state?.navigationState) {
        MainViewModel.NavigationState.InvalidEnvironment -> {
            Text("Environment Invalid")
        }
        MainViewModel.NavigationState.None -> {
            Text("Environment Valid")
        }
        MainViewModel.NavigationState.ValidEnvironment -> {
            // Proceed to main app
        }
        null -> {}
    }
}
