dependencies {
    compileOnly(projects.bearconomyApi)
    compileOnly(libs.alpinecore)
    compileOnly(libs.spigotApi) {
        exclude("junit")
        exclude("org.yaml", "snakeyaml")
    }
    compileOnly(libs.papi) {
        exclude("org.jetbrains")
    }
    compileOnly(libs.vault) {
        exclude("org.bukkit")
    }
}

tasks {
    processResources {
        expandProperties("plugin.yml")
    }
}