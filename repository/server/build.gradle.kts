import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    application
}

group = "com.motorro.repository"
version = "1.0.0"
application {
    mainClass.set("com.motorro.repository.server.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":repository:data"))
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status)
    implementation(libs.ktor.server.contentType)
    implementation(libs.ktor.serialization.contentJson)
    implementation(libs.ktor.server.auth)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk.mockk)
}

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.optIn.add("kotlin.uuid.ExperimentalUuidApi")
}
tasks.named<KotlinCompilationTask<*>>("compileTestKotlin").configure {
    compilerOptions.optIn.add("kotlin.uuid.ExperimentalUuidApi")
}