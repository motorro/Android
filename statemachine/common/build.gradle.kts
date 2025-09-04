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
        namespace = "com.motorro.statemachine.common"
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
            baseName = "common"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.compose.multiplatform.runtime)
                api(libs.compose.multiplatform.foundation)
                api(libs.compose.multiplatform.material3)
                api(libs.compose.multiplatform.material3.icons)
                api(libs.compose.multiplatform.ui)
                api(libs.compose.multiplatform.components.resources)
                api(libs.kotlinx.coroutines)
                implementation(libs.napier)
                implementation(libs.commonstatemachine.machine)
                implementation(libs.commonstatemachine.coroutines)
                implementation(libs.compose.multiplatform.uiToolingPreview)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.compose.multiplatform.uiToolingPreview)
                implementation(libs.androidx.activity.compose)
            }
        }
        iosMain {
            dependencies {

            }
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.motorro.statemachine.common"
    generateResClass = always
}

