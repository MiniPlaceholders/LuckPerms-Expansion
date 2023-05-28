plugins {
    alias(libs.plugins.runpaper)
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.luckperms)
    implementation(projects.luckpermsExpansionCommon)
}

tasks {
    processResources {
        filesMatching("paper-plugin.yml") {
            expand("version" to project.version)
        }
    }
}
