import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
}

val vName = "1.15.192"
val vCode = vName.split(".").map { it.toInt() }.let { (major, minor, patch) ->
    major * 100000 + minor * 1000 + patch
}

println("--------VERSION--------")
println("versionName: $vName")
println("versionCode: $vCode")

android {
    namespace = "com.motorro.release"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.motorro.release"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = vCode
        versionName = vName
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":composecore"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.icons)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.immutable)
    implementation(libs.commonstatemachine.machine)
    implementation(libs.commonstatemachine.coroutines)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.napier)
}