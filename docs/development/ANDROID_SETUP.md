# Android Setup Guide for Aahara

This guide covers the necessary steps to configure the Android development environment for the Aahara React Native project on Windows, targeting physical device deployment.

## 1. Required Software
- **Node.js**: v22.17.1 (Already installed)
- **Java**: Java 21 is required (Already installed, `JAVA_HOME` is set to `C:\Users\gowth\.jdks\ms-21.0.6`)
- **Android Studio**: Requires manual installation to configure the Android SDK properly.

## 2. Android Studio & SDK Installation (Manual Step)
1. Download the latest stable Android Studio installer from [developer.android.com/studio](https://developer.android.com/studio).
2. Run the installer (requires Administrator privileges).
3. Follow the Android Studio Setup Wizard on first launch. Ensure the following components are selected:
   - **Android SDK**
   - **Android SDK Platform** (Version 37 / API 37 required by this project)
   - **Android SDK Platform-Tools**
   - **Android SDK Build-Tools** (Version 37.0.0)
4. Take note of the Android SDK Location displayed in the wizard (typically `C:\Users\<YourUsername>\AppData\Local\Android\Sdk`).

## 3. Environment Variables
Once the SDK is installed, open PowerShell as Administrator or use the Windows Advanced System Settings to configure the environment variables:

```powershell
# Set ANDROID_HOME
[System.Environment]::SetEnvironmentVariable("ANDROID_HOME", "$env:LOCALAPPDATA\Android\Sdk", "User")

# Add SDK tools to PATH
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", "User")
$newPath = "$currentPath;$env:LOCALAPPDATA\Android\Sdk\platform-tools;$env:LOCALAPPDATA\Android\Sdk\emulator;$env:LOCALAPPDATA\Android\Sdk\cmdline-tools\latest\bin"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, "User")
```

Restart your terminal after applying these changes.

## 4. ADB Verification & Physical Device Setup
1. Enable **Developer Options** on your physical Android device (Tap 'Build Number' 7 times in Settings > About Phone).
2. Enable **USB Debugging** in Developer Options.
3. Connect the phone to your PC via USB.
4. Verify the connection using ADB:
   ```powershell
   adb version
   adb devices
   ```
   You should see your device listed as `device` (accept the RSA key prompt on the phone if it appears).

## 5. Metro Workflow (Physical Device)
To allow the app running on the physical phone to connect to the local Metro bundler over USB:
1. Run the reverse TCP command:
   ```powershell
   adb reverse tcp:8081 tcp:8081
   ```
   *Note: Do not configure the app to use `10.0.2.2`. That IP is strictly for the Android Emulator.*
2. Start the Metro bundler:
   ```powershell
   npm run start
   ```

## 6. Running the App
With the device connected and Metro running, build and deploy the app:
```powershell
npm run android
```

## 7. Common Troubleshooting
- **No devices found / adb not recognized**: Ensure `platform-tools` is in your `PATH` and USB debugging is enabled on your device.
- **Metro connection error**: Ensure `adb reverse tcp:8081 tcp:8081` has been executed. Make sure both device and PC do not have aggressive firewalls blocking localhost forwarding.
- **Java version mismatch**: Ensure `JAVA_HOME` points exactly to the JDK 21 path (as expected by Gradle 9.4).
