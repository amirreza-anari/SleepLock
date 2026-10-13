# 🌙 SleepTouch

<p align="center">
  <img src="app/src/main/res/mipmap-xxhdpi/ic_launcher_round.webp" width="120" height="120" alt="SleepTouch Logo" />
</p>

<p align="center">
  <b>Prevent unauthorized access to your phone while you sleep.</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin-purple.svg" alt="Language" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg" alt="UI" />
  <img src="https://img.shields.io/badge/License-MIT-orange.svg" alt="License" />
</p>

---

## 📖 About SleepTouch

**SleepTouch** is an Android security application designed to protect your device privacy while you are asleep. Before going to bed, set a temporary 4-digit PIN for the night. Once activated, SleepTouch enforces a full-screen lock overlay and system kiosk mode to block unauthorized phone access.

---

## ✨ Features

- 🌙 **Pure All-Dark Theme**: Designed specifically for dark environments with an eye-friendly OLED black theme.
- 🔢 **Temporary 4-Digit Night PIN**: Fast PIN setup using a custom on-screen numeric keypad.
- ☝️ **Emergency Biometric Fingerprint Unlock**: Instant fallback using Android's `BiometricPrompt` if you forget your PIN or need emergency access.
- 🛡️ **Kiosk & Lock Task Mode**: Prevents bypassing the lock screen via Home, Recents (App Switcher), or Back button gestures.
- 📱 **System Overlay Coverage**: Uses `SYSTEM_ALERT_WINDOW` to display a floating lock screen over all other apps and system windows.
- 🕒 **High-Visibility Clock**: Large, high-contrast clock and date display suitable for nighttime viewing.

---

## 🛠️ Built With

* **Language**: [Kotlin](https://kotlinlang.org/)
* **UI Framework**: [Jetpack Compose (Material 3)](https://developer.android.com/jetpack/compose)
* **Authentication**: [AndroidX Biometric API](https://developer.android.com/training/sign-in/biometric-auth)
* **System APIs**: Kiosk Lock Task, System Alert Window Overlay, Foreground Service

---

## 🔐 Permissions Required

| Permission | Purpose |
| :--- | :--- |
| `SYSTEM_ALERT_WINDOW` | Required to display the full-screen lock overlay over all other applications. |
| `USE_BIOMETRIC` | Enables emergency fingerprint unlock. |
| `FOREGROUND_SERVICE` | Keeps the lock protection service active in the background. |

---

## 🚀 Getting Started

### Prerequisites

* Android Studio (Ladybug or newer)
* Android SDK 24+ (Android 7.0 Nougat or higher)
* JDK 11 or higher

### Building & Running

1. Clone the repository:
   ```bash
   git clone https://github.com/amirreza-anari/SleepTouch.git
   cd SleepTouch
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle dependencies:
   ```bash
   ./gradlew build
   ```
4. Run the app on a connected Android device or emulator.

---

## 📄 License

```text
MIT License

Copyright (c) 2026 Amirreza Anari

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
