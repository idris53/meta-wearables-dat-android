# Meta Wearables DAT Library Module

An Android library module that provides ready-to-use components for integrating Meta's Wearables Device Access Toolkit into your applications.

## What's This Module?

This is a library module converted from the Meta Wearables CameraAccess sample app. It provides:

- ✅ Complete UI components for wearable camera access
- ✅ ViewModels for state management
- ✅ Video streaming capabilities
- ✅ Photo capture functionality
- ✅ Mock device support for testing

## Quick Start

### 1. Add as Dependency

**Using Git Submodule:**
```bash
git submodule add https://github.com/facebook/meta-wearables-dat-android.git modules/meta-wearables-dat
```

**In your `settings.gradle.kts`:**
```kotlin
include(":meta-wearables-dat")
project(":meta-wearables-dat").projectDir = 
    file("modules/meta-wearables-dat/samples/CameraAccess/meta-wearables-dat")
```

**In your app's `build.gradle.kts`:**
```kotlin
dependencies {
    implementation(project(":meta-wearables-dat"))
}
```

### 2. Configure GitHub Token

Create `local.properties` in your project root:
```properties
github_token=your_github_token_with_read_packages_scope
```

### 3. Update AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.INTERNET" />

<application>
    <meta-data
        android:name="com.meta.wearable.mwdat.APPLICATION_ID"
        android:value="your_app_id_from_wearables_developer_center" />
</application>
```

### 4. Initialize and Use

```kotlin
import com.meta.wearable.mwdat.Wearables
import com.meta.wearable.dat.externalsampleapps.cameraaccess.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Wearables.initialize(applicationContext)
        
        setContent {
            val wearablesViewModel: WearablesViewModel = viewModel()
            HomeScreen(wearablesViewModel = wearablesViewModel)
        }
    }
}
```

## What You Get

### UI Components
- `HomeScreen`: Main navigation and device status
- `StreamScreen`: Live video streaming from wearables
- `NonStreamScreen`: Photo capture and gallery
- `MockDeviceKitScreen`: Testing with simulated devices

### ViewModels
- `WearablesViewModel`: Device connection and management
- `StreamViewModel`: Video streaming control
- `MockDeviceKitViewModel`: Mock device operations

### Resources
- Material 3 themes and colors
- Icon drawables
- String resources

## Requirements

- **Min SDK**: 31 (Android 12)
- **Compile SDK**: 35
- **Kotlin**: 2.1.20
- **Compose**: Yes (Material 3)
- **GitHub Token**: Required for Meta SDK dependencies

## Module Info

- **Package**: `com.meta.wearable.dat.externalsampleapps.cameraaccess`
- **Type**: Android Library (`.aar`)
- **Dependencies**: mwdat-core, mwdat-camera, mwdat-mockdevice

## Documentation

- 📖 [Complete Integration Guide](MODULE_USAGE.md)
- 🌐 [Meta Wearables Developer Center](https://wearables.developer.meta.com/)
- 📚 [API Reference](https://wearables.developer.meta.com/docs/reference/android/dat/0.4)

## Need Help?

- Check the [detailed MODULE_USAGE.md](MODULE_USAGE.md) for comprehensive instructions
- Visit [GitHub Discussions](https://github.com/facebook/meta-wearables-dat-android/discussions)
- Review the [main README](../../README.md) for SDK information

## License

Licensed under the license found in the LICENSE file in the root directory.
