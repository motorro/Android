import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatformAndroidLibrary)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()

    @Suppress("UnstableApiUsage")
    androidLibrary {
        namespace = "com.motorro.statemachine.login"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        androidResources.enable = true

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "login"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":statemachine:common"))
                implementation(libs.commonstatemachine.machine)
                implementation(libs.commonstatemachine.coroutines)
                implementation(compose.components.uiToolingPreview)
            }
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.motorro.statemachine.login"
    generateResClass = always
}

