plugins {
    id("java")
    alias(libs.plugins.shadow)
}

allprojects {
    apply(plugin = "java")

    group = "${rootProject.properties["maven_group"]}.${rootProject.properties["maven_artifact"]}"
    version = "${rootProject.properties["version"]}"

    val props = mapOf(
        "mavenArtifact" to rootProject.properties["maven_artifact"],
        "pluginName" to rootProject.properties["plugin_name"],
        "pluginDescription" to rootProject.properties["plugin_description"],
        "pluginVersion" to rootProject.version,
        "pluginGroup" to rootProject.group,
    )

    repositories {
        mavenCentral()
        maven("https://repo.codemc.org/repository/nms/")
        maven("https://lib.alpn.cloud/alpine-public/")
        maven("https://lib.alpn.cloud/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
    }

    val libs = rootProject.libs
    dependencies {
        compileOnly(libs.spigot.api)
        compileOnly(libs.alpinecore)

        compileOnly(libs.papi)
        compileOnly(libs.vault)

        compileOnly(libs.lombok)
        annotationProcessor(libs.lombok)
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<JavaCompile> {
            val languageLevel = JavaVersion.VERSION_1_8.toString();
            sourceCompatibility = languageLevel
            targetCompatibility = languageLevel
        }

        processResources {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            inputs.properties(props)
            filesMatching("plugin.yml") {
                expand(props)
            }
            filesMatching("bungee.yml") {
                expand(props)
            }
        }
    }
}

// Create the shadowed jar (API + Impl)
dependencies {
    listOf(project(":plugin-common"), project(":plugin-api")).forEach {
        shadow(it) { isTransitive = false }
    }
}

sourceSets {
    main {
        resources.srcDirs += project(":plugin-common").file("src/main/resources")
    }
}

tasks {
    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        archiveClassifier.set("dev-shadow")
        archiveFileName.set("${rootProject.properties["plugin_name"]}-${rootProject.version}.jar")
    }

    jar {
        archiveClassifier.set("dev")
    }

    build {
        dependsOn(shadowJar)
    }
}
