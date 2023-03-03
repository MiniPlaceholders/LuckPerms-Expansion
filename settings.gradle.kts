rootProject.name = "Luckperms-Expansion"

arrayOf("paper", "velocity").forEach {
    include("luckperms-expansion-$it")
    project(":luckperms-expansion-$it").projectDir = file(it)
}
