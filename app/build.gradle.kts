import info.shell.runCommand

plugins {
    id("com.android.application")
    id("kotlin-android")
}

if (useFirebase() == "true") {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

android {
    namespace = "info.hannes.logcat.app"
    testNamespace = "info.hannes.logcat.sampletest"

    defaultConfig {
        applicationId = "info.hannes.logcat.sample"
        versionCode = 1
        versionName = "1.0"

        minSdk = 23
        compileSdk = 36
        targetSdk { version = release(36) }

        buildConfigField("boolean", "WITH_FIREBASE", useFirebase())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments.putAll(
            mapOf(
                "useTestStorageService" to "true",
            ),
        )
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
        }
        debug {
        }
    }
    packaging {
        resources {
            pickFirsts += setOf("META-INF/atomicfu.kotlin_module")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":LogcatCoreLib"))
    implementation(project(":LogcatCoreUI"))
    implementation(project(":LogcatCrashlyticLib"))
    implementation(project(":LogcatCountlyLib"))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")

    androidTestImplementation("androidx.test:rules:1.7.0") //    GrantPermissionRule
    androidTestImplementation("com.github.AppDevNext:Moka:1.8")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.3.0")
    androidTestUtil("androidx.test.services:test-services:1.6.0")
    androidTestImplementation("org.hamcrest:hamcrest:3.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.7.0")
}

fun useFirebase(): String {
    val process = "grep DUMMYKEY app/google-services.json -R | wc -c".runCommand()
    val grepResult = process.trim().split(":")[1].toInt()
    val result = grepResult != 1
    println("Firebase=$result")
    return result.toString()
}
