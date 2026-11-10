# CardQuiz 🃏

An Android quiz application project with authentication screens, game rules, quiz activities, and XML-based question parsing.

## Features represented in the source

- Splash screen that transitions to the authentication flow
- Login and registration activities
- Home and game-rules screens
- Quiz activity and question model
- XML Pull Parser utility for loading questions, answer options, and correct answers from an XML input stream
- Separate portrait and landscape layouts

## Tech Stack

- Java
- Android SDK (compile / target SDK 34; minimum SDK 24)
- AndroidX AppCompat
- Material Components
- ConstraintLayout
- XML layouts and XML Pull Parser
- JUnit and Espresso test dependencies

## Run Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/SRIKALYANBHAVANTHI/CardQuiz.git
   ```
2. Open the project in Android Studio.
3. Allow Gradle sync to complete.
4. Run the **app** configuration on an Android emulator or device.

Build a debug APK from the project root:

```bash
./gradlew assembleDebug
```

Windows:

```bat
gradlew.bat assembleDebug
```

## Project Structure

Application source is under `app/src/main/java/com/example/cardquiz`; Android layouts and resources are under `app/src/main/res`.

## Notes

The repository contains the application source and Android project configuration. Runtime behavior and a successful build have not been independently tested as part of this documentation update.

---

Created by [Srikalyan Bhavanthi](https://github.com/SRIKALYANBHAVANTHI).
