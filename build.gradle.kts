import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    apply(plugin = "java")
    group = "me.dreamerzero.example"
    version = "1.0.0"
    description = "Template-Expansion"
}

dependencies {
    shadow(project(":example-velocity"))
    shadow(project(":example-paper"))
}

subprojects {
    repositories {
		mavenCentral()
        maven("https://jitpack.io")
        maven("https://papermc.io/repo/repository/maven-public/")
    }
	
	dependencies {
		compileOnly("com.github.4drian3d:MiniPlaceholders:1.0.0")
	}
}

tasks {
    shadowJar {
        archiveFileName.set("Example-Expansion.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.shadow.get())
    }
    build {
        dependsOn(shadowJar)
    }
}
