plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
kotlin {
    jvmToolchain(21)
}

dependencies {
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.datetime)
    api(libs.javax.inject.annotations)
}