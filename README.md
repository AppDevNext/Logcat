[![](https://jitpack.io/v/AppDevNext/Logcat.svg)](https://jitpack.io/#AppDevNext/Logcat)

# Show Logcat in release buildtype and Timber log when it logs to a file

This library provides an easy way to show Logcat in **release** ! and Timber history, which was written to file.
Right Timber `FileLoggingTree.kt` is provided too  

![Screenshot](screenshot.png)

## Include the library

The easiest way to add Logcat to your project is via Gradle. Just add the following lines to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.AppDevNext.Logcat:LogcatCore:$latest_version'
    implementation 'com.github.AppDevNext.Logcat:LogcatCrashlytic:$latest_version'
}
```

To tell Gradle where to find the library, make sure `build.gradle` also contains this:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
In `AndroidManifest.xml`
```
    <application
        android:name="info.hannes.logcat.LoggingApplication"
```
or for file logging
```
    <application
        android:name="info.hannes.logcat.FileLoggingApplication"
```

## Crashlytic integration

### Features

* Get crash reports
* Get non-fatal errors into Firebase Crashlytics dashboard in an easy way with Timber
* Disable sending the reports to Firebase Crashlytics in DEBUG mode
* A crash will be reported automatically
* Logs a non-fatal via Timber.e() or Timber.w()

        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }

        FirebaseCrashlytics.getInstance().setCustomKey("VERSION_NAME", BuildConfig.VERSION_NAME)
        Timber.plant(CrashlyticsTree(Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)))

### Using newest version of the lib in a Kotlin 1.3.xx project

To avoid build failure due to higher version of kotlinx-coroutines-android (you are then responsible for loading the excluded required dependencies)

        implementation('com.github.AppDevNext.Logcat:LogcatCore:$latest_version') {
            exclude group: 'androidx.lifecycle'
            exclude group: 'org.jetbrains.kotlin'
            exclude group: 'org.jetbrains.kotlinx'
        }


## License

    Copyright (C) 2012-2021 AppDevNext

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
