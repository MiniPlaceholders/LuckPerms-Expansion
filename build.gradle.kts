plugins {
    java
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.luckpermsExpansionPaper)
    implementation(projects.luckpermsExpansionVelocity)
}

subprojects {
    apply<JavaPlugin>()
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
        }
    }
}

tasks {
    shadowJar {
        archiveFileName.set("MiniPlaceholders-LuckPerms-Expansion-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    build {
        dependsOn(shadowJar)
    }
}
