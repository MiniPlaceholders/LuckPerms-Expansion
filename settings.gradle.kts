rootProject.name = "example-parent"

include("example-paper")
include("example-velocity")

project(":example-velocity").projectDir = file("velocity")
project(":example-paper").projectDir = file("paper")
