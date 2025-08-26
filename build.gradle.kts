plugins {
    id("bearconomy.shadow-conventions")
}

subprojects {
    apply {
        plugin("bearconomy.base-conventions")
        if (project.name != "example") {
            plugin("bearconomy.spotless-conventions")
        }
    }
}
