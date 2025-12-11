plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "info.hannes.logcat.crashlytic"
    defaultConfig {
        compileSdk = 36
        minSdk = 23
        setProperty("archivesBaseName", "LogcatCrashlytic")
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
    // Import the BoM for the Firebase platform
    api(platform("com.google.firebase:firebase-bom:34.5.0"))

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don"t specify versions in Firebase library dependencies
    api("com.google.firebase:firebase-crashlytics")
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
