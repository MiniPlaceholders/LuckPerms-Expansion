dependencies {
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.luckperms)
    implementation(projects.luckpermsExpansionCommon)
}
