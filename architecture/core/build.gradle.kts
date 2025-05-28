plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    api(libs.kotlinx.datetime)
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.serialization.core)
}

kotlin {
    compilerOptions.optIn.add("kotlin.uuid.ExperimentalUuidApi")
}


