# Using Meta Wearables DAT as a Module

This guide explains how to import and use the Meta Wearables Device Access Toolkit module in your Android project.

## Overview

The CameraAccess sample has been converted into a library module (`meta-wearables-dat`) that you can import into your own Android application. This module provides:

- Complete UI components for wearable camera access
- ViewModels for managing wearables state and streaming
- Integration with Meta's Wearables SDK (mwdat-core, mwdat-camera, mwdat-mockdevice)
- Ready-to-use Compose UI screens

## Prerequisites

- Android Studio Arctic Fox or later
- Minimum SDK: 31 (Android 12)
- Target SDK: 34 or higher
- Kotlin 1.9.0 or later
- GitHub Personal Access Token with `read:packages` scope (to access Meta's SDK)

## Setup Your GitHub Token

Before importing the module, you need to set up authentication for GitHub Packages:

### Option 1: Environment Variable (Recommended)
```bash
export GITHUB_TOKEN=your_github_personal_access_token
```

### Option 2: local.properties File
Add to your project's `local.properties`:
```properties
github_token=your_github_personal_access_token
```

## Method 1: Import as Git Submodule (Recommended)

This method keeps the module code separate and allows for easy updates.

### Step 1: Add as Submodule

```bash
cd your-project-root
git submodule add https://github.com/facebook/meta-wearables-dat-android.git modules/meta-wearables-dat
git submodule update --init --recursive
```

### Step 2: Include in settings.gradle.kts

Add to your project's `settings.gradle.kts`:

```kotlin
include(":meta-wearables-dat")
project(":meta-wearables-dat").projectDir = file("modules/meta-wearables-dat/samples/CameraAccess/meta-wearables-dat")
```

### Step 3: Add Repositories

Ensure your `settings.gradle.kts` includes the necessary repositories:

```kotlin
import java.util.Properties
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.inputStream

pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

val localProperties = Properties().apply {
  val localPropertiesPath = rootDir.toPath() / "local.properties"
  if (localPropertiesPath.exists()) {
    load(localPropertiesPath.inputStream())
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
  repositories {
    google()
    mavenCentral()
    // Required for Meta Wearables SDK
    maven {
      url = uri("https://maven.pkg.github.com/facebook/meta-wearables-dat-android")
      credentials {
        username = "" // not needed
        password = System.getenv("GITHUB_TOKEN") ?: localProperties.getProperty("github_token")
      }
    }
  }
}
```

### Step 4: Add Dependency

In your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":meta-wearables-dat"))
    // Other dependencies...
}
```

## Method 2: Import as Local Module

This method copies the module directly into your project.

### Step 1: Copy Module

```bash
# From the meta-wearables-dat-android repository
cp -r samples/CameraAccess/meta-wearables-dat /path/to/your-project/
```

### Step 2: Include in settings.gradle.kts

Add to your project's `settings.gradle.kts`:

```kotlin
include(":meta-wearables-dat")
```

### Step 3: Configure Repositories (same as Method 1, Step 3)

### Step 4: Add Dependency (same as Method 1, Step 4)

## Using the Module in Your App

### 1. Configure AndroidManifest.xml

Add required permissions and metadata to your app's `AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Required permissions -->
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application>
        <!-- Required: Your application ID from Wearables Developer Center -->
        <meta-data
            android:name="com.meta.wearable.mwdat.APPLICATION_ID"
            android:value="your_app_id_here" />

        <!-- Optional: Disable analytics -->
        <meta-data
            android:name="com.meta.wearable.mwdat.ANALYTICS_OPT_OUT"
            android:value="false" />

        <!-- Your activities and components -->
    </application>
</manifest>
```

### 2. Initialize Wearables in Your Application

In your main Activity or Application class:

```kotlin
import com.meta.wearable.mwdat.Wearables

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Wearables SDK
        Wearables.initialize(applicationContext)
        
        setContent {
            // Your app content
        }
    }
}
```

### 3. Use Provided Components

You can use the pre-built UI components and ViewModels:

```kotlin
import com.meta.wearable.dat.externalsampleapps.cameraaccess.ui.HomeScreen
import com.meta.wearable.dat.externalsampleapps.cameraaccess.ui.StreamScreen
import com.meta.wearable.dat.externalsampleapps.cameraaccess.wearables.WearablesViewModel

@Composable
fun YourAppScreen() {
    val wearablesViewModel: WearablesViewModel = viewModel()
    
    // Use the provided screens
    HomeScreen(
        wearablesViewModel = wearablesViewModel,
        onNavigateToStream = { /* navigation logic */ }
    )
}
```

### 4. Access Wearables Features

The module exposes ViewModels that manage:

- **WearablesViewModel**: Device connection and registration
- **StreamViewModel**: Video streaming from wearables
- **MockDeviceKitViewModel**: Testing with mock devices

Example:

```kotlin
@Composable
fun MyWearablesFeature() {
    val wearablesViewModel: WearablesViewModel = viewModel()
    val uiState by wearablesViewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        wearablesViewModel.startMonitoring()
    }
    
    when (uiState) {
        is WearablesUiState.Connected -> {
            // Device is connected
            Text("Device connected!")
        }
        is WearablesUiState.Disconnected -> {
            // No device connected
            Button(onClick = { wearablesViewModel.startRegistration(activity) }) {
                Text("Connect Device")
            }
        }
        // Handle other states...
    }
}
```

## Module Structure

```
meta-wearables-dat/
├── src/
│   ├── main/
│   │   ├── java/com/meta/wearable/dat/externalsampleapps/cameraaccess/
│   │   │   ├── MainActivity.kt
│   │   │   ├── ui/              # Compose UI components
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   ├── StreamScreen.kt
│   │   │   │   ├── NonStreamScreen.kt
│   │   │   │   └── ...
│   │   │   ├── wearables/       # Wearables management
│   │   │   │   ├── WearablesViewModel.kt
│   │   │   │   └── WearablesUiState.kt
│   │   │   ├── stream/          # Video streaming
│   │   │   │   ├── StreamViewModel.kt
│   │   │   │   └── StreamUiState.kt
│   │   │   └── mockdevicekit/   # Mock device testing
│   │   │       ├── MockDeviceKitViewModel.kt
│   │   │       └── MockDeviceKitUiState.kt
│   │   ├── res/                 # Resources (drawables, strings, themes)
│   │   └── AndroidManifest.xml
│   └── androidTest/             # Instrumentation tests
├── build.gradle.kts
└── proguard-rules.pro
```

## Key Features

### 1. Device Connection
- Automatic device discovery
- Registration and pairing workflow
- Connection state management

### 2. Camera Streaming
- Real-time video streaming from wearables
- Frame processing and display
- Quality and frame rate configuration

### 3. Photo Capture
- Capture photos from wearables
- Save and share functionality
- EXIF metadata handling

### 4. Mock Device Testing
- Test without physical hardware
- Simulate device behaviors
- Development and testing support

## Dependencies

The module requires these dependencies (automatically included):

```kotlin
// Meta Wearables SDK
implementation("com.meta.wearable:mwdat-core:0.4.0")
implementation("com.meta.wearable:mwdat-camera:0.4.0")
implementation("com.meta.wearable:mwdat-mockdevice:0.4.0")

// AndroidX and Compose
implementation("androidx.activity:activity-compose:1.10.1")
implementation("androidx.compose.material3:material3")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
// ... and more
```

## Customization

### Theme and Colors

The module uses Material 3 theming. You can customize by:

1. Overriding the theme in your app
2. Modifying `ui/AppColor.kt` in the module
3. Providing your own theme to composables

### UI Components

All UI components are modular and can be:
- Used as-is in your app
- Customized by forking and modifying
- Replaced with your own implementations using the ViewModels

## Troubleshooting

### Build Errors

**Issue**: "Plugin [id: 'com.android.library'] was not found"

**Solution**: Ensure your `settings.gradle.kts` has correct plugin repositories:
```kotlin
pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
```

**Issue**: "Could not resolve com.meta.wearable:mwdat-core:0.4.0"

**Solution**: Check that your GitHub token is properly configured and has `read:packages` scope.

### Runtime Issues

**Issue**: "WearablesException: Wearables not initialized"

**Solution**: Call `Wearables.initialize(context)` before using any wearables features.

**Issue**: Permission denied errors

**Solution**: Ensure all required permissions are declared in AndroidManifest.xml and requested at runtime.

## Support and Documentation

- [Meta Wearables Developer Center](https://wearables.developer.meta.com/)
- [API Reference](https://wearables.developer.meta.com/docs/reference/android/dat/0.4)
- [GitHub Discussions](https://github.com/facebook/meta-wearables-dat-android/discussions)

## License

This module is licensed under the license found in the LICENSE file in the root directory of this source tree.

By using the Wearables Device Access Toolkit, you agree to the [Meta Wearables Developer Terms](https://wearables.developer.meta.com/terms).
