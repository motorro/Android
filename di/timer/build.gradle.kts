plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.datetime)
    api(libs.javax.inject.annotations)
}