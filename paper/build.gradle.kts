plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

val pluginVersion = version

bukkit {
    main = "me.dreamerzero.example.paper.PaperPlugin"
    apiVersion = "1.18"
    authors = listOf("4drian3d")
    depend = listOf("MiniPlaceholders")
    version = pluginVersion as String
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
}

