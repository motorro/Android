plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
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
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk.mockk)
}
