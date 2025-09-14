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

    android {
        namespace = "com.motorro.statemachine.register"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        androidResources.enable = true

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "register"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":statemachine:common"))
                implementation(libs.commonstatemachine.machine)
                implementation(libs.commonstatemachine.coroutines)
                implementation(libs.compose.multiplatform.uiToolingPreview)
            }
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.motorro.statemachine.register"
    generateResClass = always
}

