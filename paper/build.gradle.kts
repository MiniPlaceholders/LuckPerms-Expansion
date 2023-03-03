plugins {
    alias(libs.plugins.pluginyml)
    alias(libs.plugins.runpaper)
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.luckperms)
}

bukkit {
    main = "io.github.miniplaceholders.expansion.luckperms.paper.PaperPlugin"
    apiVersion = "1.18"
    authors = listOf("4drian3d")
    depend = listOf("MiniPlaceholders", "LuckPerms")
    version = project.version as String
}
