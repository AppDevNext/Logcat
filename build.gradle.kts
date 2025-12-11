buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") } // ktlint
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.13.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:12.1.2")
        classpath("com.google.gms:google-services:4.4.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(false)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
