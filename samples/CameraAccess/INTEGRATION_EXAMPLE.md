# Example: Using Meta Wearables DAT Module in Your App

This example shows how to integrate and use the Meta Wearables DAT library module in a typical Android application.

## Project Structure

```
your-app/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   └── java/com/yourcompany/yourapp/
│   │       └── MainActivity.kt
│   └── build.gradle.kts
├── meta-wearables-dat/  (submodule or copied module)
├── settings.gradle.kts
├── build.gradle.kts
└── local.properties
```

## Complete Integration Example

### 1. settings.gradle.kts

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
                username = ""
                password = System.getenv("GITHUB_TOKEN") ?: localProperties.getProperty("github_token")
            }
        }
    }
}

rootProject.name = "YourApp"
include(":app")
include(":meta-wearables-dat")

// Option A: If using git submodule
project(":meta-wearables-dat").projectDir = 
    file("modules/meta-wearables-dat/samples/CameraAccess/meta-wearables-dat")

// Option B: If copied directly to your project
// project(":meta-wearables-dat").projectDir = file("meta-wearables-dat")
```

### 2. app/build.gradle.kts

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.yourcompany.yourapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yourcompany.yourapp"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Import the Meta Wearables DAT module
    implementation(project(":meta-wearables-dat"))
    
    // Your other dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
}
```

### 3. app/src/main/AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Required permissions from Meta Wearables DAT -->
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.YourApp">

        <!-- Required: Application ID from Wearables Developer Center -->
        <!-- Get your ID from https://wearables.developer.meta.com/ -->
        <meta-data
            android:name="com.meta.wearable.mwdat.APPLICATION_ID"
            android:value="YOUR_APP_ID_HERE" />

        <!-- Optional: Opt out of analytics -->
        <meta-data
            android:name="com.meta.wearable.mwdat.ANALYTICS_OPT_OUT"
            android:value="false" />

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.YourApp">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

### 4. MainActivity.kt - Example 1: Using Pre-built Screens

```kotlin
package com.yourcompany.yourapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meta.wearable.mwdat.Wearables
import com.meta.wearable.dat.externalsampleapps.cameraaccess.ui.HomeScreen
import com.meta.wearable.dat.externalsampleapps.cameraaccess.ui.StreamScreen
import com.meta.wearable.dat.externalsampleapps.cameraaccess.wearables.WearablesViewModel
import com.meta.wearable.dat.externalsampleapps.cameraaccess.stream.StreamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Wearables SDK - REQUIRED!
        Wearables.initialize(applicationContext)
        
        setContent {
            YourAppWithWearables()
        }
    }
}

@Composable
fun YourAppWithWearables() {
    val navController = rememberNavController()
    val wearablesViewModel: WearablesViewModel = viewModel()
    val streamViewModel: StreamViewModel = viewModel()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            // Use the pre-built home screen
            HomeScreen(
                wearablesViewModel = wearablesViewModel,
                onNavigateToStream = { navController.navigate("stream") },
                onNavigateToNonStream = { /* handle */ },
                onNavigateToMockDevice = { /* handle */ }
            )
        }
        
        composable("stream") {
            // Use the pre-built streaming screen
            StreamScreen(
                wearablesViewModel = wearablesViewModel,
                streamViewModel = streamViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

### 5. MainActivity.kt - Example 2: Custom Integration

```kotlin
package com.yourcompany.yourapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meta.wearable.mwdat.Wearables
import com.meta.wearable.dat.externalsampleapps.cameraaccess.wearables.WearablesViewModel
import com.meta.wearable.dat.externalsampleapps.cameraaccess.wearables.WearablesUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Wearables.initialize(applicationContext)
        
        setContent {
            CustomWearablesIntegration()
        }
    }
}

@Composable
fun CustomWearablesIntegration() {
    val wearablesViewModel: WearablesViewModel = viewModel()
    val uiState by wearablesViewModel.uiState.collectAsState()
    
    // Start monitoring for devices
    LaunchedEffect(Unit) {
        wearablesViewModel.startMonitoring()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Wearables App") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is WearablesUiState.Idle -> {
                    Text("Initializing...")
                }
                
                is WearablesUiState.Disconnected -> {
                    Text("No device connected")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            wearablesViewModel.startRegistration(
                                activity = this@MainActivity
                            )
                        }
                    ) {
                        Text("Connect Wearable Device")
                    }
                }
                
                is WearablesUiState.Connected -> {
                    val state = uiState as WearablesUiState.Connected
                    Text("✓ Connected to ${state.device.name}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Device ID: ${state.device.id}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Your custom UI for connected device
                    Button(onClick = { /* Start streaming */ }) {
                        Text("Start Video Stream")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { /* Capture photo */ }) {
                        Text("Capture Photo")
                    }
                }
                
                is WearablesUiState.Error -> {
                    val state = uiState as WearablesUiState.Error
                    Text(
                        "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
```

### 6. local.properties

```properties
# GitHub Personal Access Token with read:packages scope
# Get token from: https://github.com/settings/tokens
github_token=ghp_your_token_here

# Android SDK location (auto-generated by Android Studio)
sdk.dir=/path/to/Android/Sdk
```

## Key Points

1. **Always initialize**: Call `Wearables.initialize(context)` before using any wearables features
2. **Permissions**: Declare BLUETOOTH, BLUETOOTH_CONNECT, and INTERNET permissions
3. **App ID**: Register your app at Wearables Developer Center and add the ID to AndroidManifest
4. **GitHub Token**: Required to fetch Meta SDK dependencies from GitHub Packages
5. **Min SDK**: Project must target Android 12 (API 31) or higher

## Testing

### With Real Device
1. Pair Meta AI glasses with your phone
2. Run your app
3. Click "Connect Wearable Device"
4. Follow registration flow

### With Mock Device
Use `MockDeviceKitViewModel` to test without physical hardware:

```kotlin
val mockDeviceViewModel: MockDeviceKitViewModel = viewModel()

// Start mock device
LaunchedEffect(Unit) {
    mockDeviceViewModel.startMockDevice()
}
```

## Next Steps

- Explore the full module source code
- Customize UI components for your brand
- Implement error handling and edge cases
- Add analytics and logging
- Test with various device states

## Resources

- [Module README](../meta-wearables-dat/README.md)
- [Detailed Integration Guide](../MODULE_USAGE.md)
- [Meta Wearables Docs](https://wearables.developer.meta.com/)
