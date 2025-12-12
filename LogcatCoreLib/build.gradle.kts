import info.git.versionHelper.getVersionText

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    namespace = "info.hannes.logcat"
    defaultConfig {
        compileSdk = 36
        buildConfigField("String", "VERSION_NAME", "\"${getVersionText()}\"")

        minSdk = 23
        setProperty("archivesBaseName", "LogcatCore")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
    publishing {
        singleVariant("release") {}
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.2.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")
    api("com.jakewharton.timber:timber:5.0.1")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["release"])
                pom {
                    licenses {
                        license {
                            name = "Apache License Version 2.0"
                            url = "https://github.com/AppDevNext/Logcat/blob/master/LICENSE"
                        }
                    }
                }
            }
        }
    }
}
