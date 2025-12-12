import info.git.versionHelper.getVersionText

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    namespace = "info.hannes.logcat.ui"
    defaultConfig {
        compileSdk = 36
        buildConfigField("String", "VERSION_NAME", "\"${getVersionText()}\"")

        minSdk = 23
        setProperty("archivesBaseName", "LogcatCoreUI")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    publishing {
        singleVariant("release") {}
    }
}

dependencies {
    api(project(":LogcatCoreLib"))
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.2.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    api("com.google.android.material:material:1.13.0")
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
