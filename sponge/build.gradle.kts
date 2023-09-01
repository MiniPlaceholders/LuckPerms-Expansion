import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("org.spongepowered.gradle.plugin")
}

dependencies {
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.luckperms)
    implementation(projects.luckpermsExpansionCommon)
}

sponge {
    apiVersion("8.1.0")
    license("GPL-3")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("miniplaceholders-luckperms-expansion") {
        displayName("MiniPlaceholders-LuckPerms-Expansion")
        entrypoint("io.github.miniplaceholders.expansion.luckperms.sponge.SpongePlugin")
        description(project.description)
        links {
            homepage("https://github.com/MiniPlaceholders/LuckPerms-Expansion")
            source("https://github.com/MiniPlaceholders/LuckPerms-Expansion")
            issues("https://github.com/MiniPlaceholders/LuckPerms-Expansion/issues")
        }
        contributor("4drian3d") {
            description("Lead Developer")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
        dependency("miniplaceholders") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
            version("2.2.0")
        }
        dependency("luckperms") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
            version("5.4.98")
        }
    }
}