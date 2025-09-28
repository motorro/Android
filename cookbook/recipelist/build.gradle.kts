import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.motorro.cookbook.recipelist"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        viewBinding = true
    }
}

dependencies {
    implementation(project(":cookbook:core"))
    implementation(project(":cookbook:domain"))
    implementation(project(":cookbook:appcore"))
    implementation(project(":cookbook:addrecipe"))
    implementation(project(":cookbook:recipe"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3.icons)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.glide)
    implementation(libs.glide.compose)

    implementation(libs.hilt.android)
    implementation(libs.hilt.androidx)
    ksp(libs.hilt.android.compiler)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk.mockk)
}

kotlin {
    compilerOptions.optIn.add("kotlin.uuid.ExperimentalUuidApi")
}