enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Luckperms-Expansion"

arrayOf("common", "paper", "velocity", "sponge").forEach {
    include("luckperms-expansion-$it")
    project(":luckperms-expansion-$it").projectDir = file(it)
}
