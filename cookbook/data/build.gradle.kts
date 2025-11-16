import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.generateKotlin", "true")
}

android {
    namespace = "com.motorro.cookbook.data"
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

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinSerialization)

    // Datastore
    implementation(libs.androidx.datastore.ptoto)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.serialization.contentJson)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.core)
    implementation(libs.hilt.android)

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