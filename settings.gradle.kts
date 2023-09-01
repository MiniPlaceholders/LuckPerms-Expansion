enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Luckperms-Expansion"

dependencyResolutionManagement {
    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
        //maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.quiltmc.org/repository/release/")
    }
}

arrayOf("common", "paper", "velocity", "sponge", "fabric").forEach {
    include("luckperms-expansion-$it")
    project(":luckperms-expansion-$it").projectDir = file(it)
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    id("fabric-loom") version "1.1-SNAPSHOT"
    id("org.spongepowered.gradle.plugin") version "2.1.1"
}
