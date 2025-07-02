pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Android"
include(":core")
include(":composecore")
include(":cookbook:model")
include(":cookbook:core")
include(":cookbook:domain")
include(":cookbook:data")
include(":cookbook:server")
include(":cookbook:cookbook")
include(":cookbook:mockdata")
include(":cookbook:appcore")
include(":cookbook:login")
include(":cookbook:recipe")
include(":cookbook:addrecipe")
include(":cookbook:recipelist")
