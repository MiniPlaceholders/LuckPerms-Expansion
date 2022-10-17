rootProject.name = "luckpermsexpansion-parent"

include("luckpermsexpansion-paper")
include("luckpermsexpansion-velocity")

project(":luckpermsexpansion-velocity").projectDir = file("velocity")
project(":luckpermsexpansion-paper").projectDir = file("paper")
