plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    namespace = "info.hannes.logcat.countly"
    defaultConfig {
        compileSdk = 36
        minSdk = 23
        setProperty("archivesBaseName", "LogcatCountly")
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
    api("ly.count.android:sdk:25.4.4")
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
