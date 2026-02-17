# Quick Reference: Meta Wearables DAT Module

## 🚀 Quick Import (2 methods)

### Method 1: Git Submodule
```bash
git submodule add https://github.com/facebook/meta-wearables-dat-android.git modules/meta-wearables-dat
```

### Method 2: Local Copy
```bash
cp -r samples/CameraAccess/meta-wearables-dat /your-project/
```

## 📦 Add to Project

**settings.gradle.kts:**
```kotlin
include(":meta-wearables-dat")
project(":meta-wearables-dat").projectDir = file("path/to/meta-wearables-dat")
```

**app/build.gradle.kts:**
```kotlin
dependencies {
    implementation(project(":meta-wearables-dat"))
}
```

## 🔑 Setup GitHub Token

**local.properties:**
```properties
github_token=your_github_token_here
```

## 📱 Configure App

**AndroidManifest.xml:**
```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.INTERNET" />

<application>
    <meta-data
        android:name="com.meta.wearable.mwdat.APPLICATION_ID"
        android:value="YOUR_APP_ID" />
</application>
```

## 💻 Basic Usage

**MainActivity.kt:**
```kotlin
import com.meta.wearable.mwdat.Wearables
import com.meta.wearable.dat.externalsampleapps.cameraaccess.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Wearables.initialize(applicationContext)
        
        setContent {
            val viewModel: WearablesViewModel = viewModel()
            HomeScreen(wearablesViewModel = viewModel)
        }
    }
}
```

## 📚 Documentation

- **Quick Start**: `samples/CameraAccess/meta-wearables-dat/README.md`
- **Detailed Guide**: `samples/CameraAccess/MODULE_USAGE.md`
- **Code Examples**: `samples/CameraAccess/INTEGRATION_EXAMPLE.md`
- **Summary**: `PROJECT_CONVERSION_SUMMARY.md`

## 🧩 What's Included

- ✅ UI Components (Compose)
- ✅ ViewModels
- ✅ Video Streaming
- ✅ Photo Capture
- ✅ Device Management
- ✅ Mock Device Testing

## ⚙️ Requirements

- **Min SDK**: 31 (Android 12)
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Token**: GitHub PAT with `read:packages`

## 🆘 Need Help?

1. Check `MODULE_USAGE.md` - comprehensive guide
2. See `INTEGRATION_EXAMPLE.md` - working examples
3. Visit [Meta Wearables Docs](https://wearables.developer.meta.com/)
4. Join [GitHub Discussions](https://github.com/facebook/meta-wearables-dat-android/discussions)
