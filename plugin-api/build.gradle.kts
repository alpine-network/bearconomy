plugins {
    id("bearconomy.maven-conventions")
}

dependencies {
    compileOnly(libs.alpinecore)
    compileOnly(libs.spigotApi) {
        exclude("junit")
        exclude("org.yaml", "snakeyaml")
        exclude("com.google.code.gson","gson")
    }
}

tasks {
    named("build") {
        dependsOn("javadoc")
    }
    withType<Jar>().configureEach {
        includeLicenseFile()
    }
    withType<Javadoc>().configureEach {
        enabled = true
        applyLinks(
            "https://docs.oracle.com/en/java/javase/11/docs/api/",
            "https://hub.spigotmc.org/javadocs/spigot/",
            "https://javadoc.io/doc/org.jetbrains/annotations/${libs.versions.annotations.get()}/",
        )
    }
}