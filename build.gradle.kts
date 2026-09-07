plugins {
    java
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
}

dependencies {
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.adventure.legacy)
    compileOnly(libs.luckperms)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(25))

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(25)
    }
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}
