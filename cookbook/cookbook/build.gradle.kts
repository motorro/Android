import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.motorro.cookbook.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.motorro.cookbook.app"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    flavorDimensions += "data"
    flavorDimensions += "login"
    productFlavors {
        create("server") {
            dimension = "data"
        }
        create("mock") {
            dimension = "data"
            applicationIdSuffix = ".mock"
        }
        create("password") {
            dimension = "login"
            applicationIdSuffix = ".password"
        }
        create("social") {
            dimension = "login"
            applicationIdSuffix = ".social"
        }
    }
}

dependencies {
    implementation(project(":cookbook:core"))
    implementation(project(":cookbook:domain"))

    "serverImplementation"(project(":cookbook:data"))
    "mockImplementation"(project(":cookbook:mockdata"))
    "passwordImplementation"(project(":cookbook:login"))
    "socialImplementation"(project(":cookbook:loginsocial"))

    implementation(project(":cookbook:appcore"))
    implementation(project(":cookbook:recipelist"))
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)

    implementation(libs.hilt.android)
    implementation(libs.hilt.androidx)
    ksp(libs.hilt.android.compiler)

    // Startup initialization
    implementation(libs.androidx.startup)

    // Work
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.androidx)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk.mockk)
}

kotlin {
    compilerOptions.optIn.add("kotlin.uuid.ExperimentalUuidApi")
}

configurations.implementation{
    exclude(group = "com.intellij", module = "annotations")
}
