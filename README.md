# PeriodicPro

PeriodicPro is a native Android application for exploring the periodic table of elements. It combines a visual element grid with search, detailed element information, favorites, and recent-view history in a clean mobile interface.

## Features

- Interactive periodic table layout
- Element search and category filtering
- Detailed views for individual elements
- Favorites list for quick access
- Recently viewed elements
- User registration, login, and profile screens
- Settings and application help
- Privacy policy and developer information

## Screens

The app includes dedicated activities for:

- Landing and periodic table navigation
- Element search and element details
- Favorites and recent elements
- Profile and settings
- Login and registration
- Help, privacy policy, and developer information

## Technology Stack

- **Language:** Kotlin
- **Platform:** Android
- **UI:** Android XML layouts with Jetpack Compose dependencies
- **Build system:** Gradle Kotlin DSL
- **Minimum SDK:** Android 7.0 (API 24)
- **Target SDK:** API 35
- **Supporting libraries:** AndroidX, Material Components, Jetpack Lifecycle, and Compose

## Requirements

- Android Studio Meerkat or a newer compatible release
- Android SDK 35
- JDK 17
- An Android device or emulator running API 24 or later

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/Kitanoed/periodicpro.git
   cd periodicpro
   ```

2. Open the project in Android Studio.

3. Allow Gradle to synchronize and install the required SDK components.

4. Create or select an Android Virtual Device, then run the `app` configuration.

### Command-Line Build

Build a debug APK:

```bash
./gradlew assembleDebug
```

Run the local unit tests:

```bash
./gradlew test
```

Run the instrumentation tests on a connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

The debug APK is written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Project Structure

```text
app/src/main/java/com/app/periodicpro/
├── data/          # Element and list data models
├── fragments/     # Reusable navigation UI
├── helper/        # Adapters and supporting helpers
├── utils/         # Shared utility code
└── *Activity.kt   # Application screens and navigation

app/src/main/res/
├── layout/        # XML screen layouts
├── drawable/      # Icons and visual assets
├── values/        # Themes and resources
└── mipmap-*/      # Application launch icons
```

Favorites, recent elements, profile data, and app preferences are stored locally on the device using Android SharedPreferences.

## Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a focused feature branch.
3. Make and test your changes.
4. Open a pull request with a clear description of the update.

Please keep changes scoped, follow the existing Kotlin and Android conventions, and include tests for new behavior where practical.

## License

No license is currently specified in this repository. Please contact the project owner before redistributing or using the project commercially.
