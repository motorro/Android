import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
}

val vName = run {
    fun parseVersion(description: String): String {
        val match = "v(\\d+)\\.?(\\d*)\\.?(\\d*)-?(\\d*)".toRegex().find(description) ?: return "0.0.1"
        val (major, minor, patch, commits) = match.groupValues.drop(1).map { it.toIntOrNull() }
        return "$major.${minor ?: 0}.${(patch ?: 0) + (commits ?: 0)}"
    }

    val git = ProcessBuilder("git", "describe", "--tags", "--match", "v[1-9]*").start()
    val description = git.inputStream.bufferedReader().readText().trim()

    return@run parseVersion(description)
}

val vCode = vName.split(".").map { it.toInt() }.let { (major, minor, patch) ->
    major * 100000 + minor * 1000 + patch
}

println("--------VERSION--------")
println("versionName: $vName")
println("versionCode: $vCode")

val signingPropertiesFile = file("$projectDir/signing/signing.properties")
val releaseSigningDefined = signingPropertiesFile.exists()

android {
    signingConfigs {
        if (releaseSigningDefined) {
            println("Reading signing properties from $signingPropertiesFile")

            val signingProperties = Properties()
            signingProperties.load(signingPropertiesFile.inputStream())

            create("release") {
                storeFile = signingProperties.getProperty("storeFile")?.let { file(it) }
                storePassword = signingProperties.getProperty("storePassword")
                keyAlias = signingProperties.getProperty("keyAlias")
                keyPassword = signingProperties.getProperty("keyPassword")
            }
        }
    }
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
            if (releaseSigningDefined) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                // File with default rules provided by the Android Gradle Plugin
                getDefaultProguardFile("proguard-android-optimize.txt"),
                // File with your custom rules
                "proguard-rules.pro"
            )
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