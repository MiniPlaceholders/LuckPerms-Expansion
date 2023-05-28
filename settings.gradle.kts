enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Luckperms-Expansion"

arrayOf("paper", "velocity", "common").forEach {
    include("luckperms-expansion-$it")
    project(":luckperms-expansion-$it").projectDir = file(it)
}
