import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    apply(plugin = "java")
    group = "me.dreamerzero.luckpermsexpansion"
    version = "1.0.0"
    description = "LuckPerms-Expansion"
}

dependencies {
    shadow(project(":luckpermsexpansion-velocity"))
    shadow(project(":luckpermsexpansion-paper"))
}

subprojects {
    repositories {
		mavenCentral()
        maven("https://jitpack.io")
        maven("https://papermc.io/repo/repository/maven-public/")
    }
	
	dependencies {
		compileOnly("com.github.4drian3d:MiniPlaceholders:1.3.1")
        compileOnly("net.luckperms:api:5.4")
	}
}

tasks {
    shadowJar {
        archiveFileName.set("LuckPerms-Expansion.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.shadow.get())
    }
    build {
        dependsOn(shadowJar)
    }
}
