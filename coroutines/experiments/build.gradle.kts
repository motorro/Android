plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.datetime)
}